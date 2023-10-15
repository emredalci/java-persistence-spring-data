package com.example.mappingvaluetypesusertype.converter;

import com.example.mappingvaluetypesusertype.model.Item;
import com.example.mappingvaluetypesusertype.model.MonetaryAmount;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.metamodel.spi.ValueAccess;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.CompositeUserType;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Objects;
import java.util.Properties;

public class MonetaryAmountUserType implements UserType<MonetaryAmount>, DynamicParameterizedType,
        CompositeUserType<MonetaryAmount> {

    public static final BigDecimal CURRENCY_USD_CONSTANT = BigDecimal.valueOf(2);

    private Currency convertTo;

    @Override
    public int getSqlType() {
        return SqlTypes.VARCHAR;
    }

    @Override
    public Object getPropertyValue(MonetaryAmount component, int property) throws HibernateException {
        if (property == 0) { return component.value(); }
        return component.currency();
    }

    @Override
    public MonetaryAmount instantiate(ValueAccess values, SessionFactoryImplementor sessionFactory) {
        return null;
    }

    @Override
    public Class<?> embeddable() {
        return Item.class;
    }

    /*
         The method <code>returnedClass</code> adapts the given class
         */
    @Override
    public Class<MonetaryAmount> returnedClass() {
        return MonetaryAmount.class;
    }

    /*
     Called to read the ResultSet, when a MonetaryAmount value has to be retrieved from the database.
     We take the amount and currency values as given in the query result, and create a new instance of MonetaryAmount.
     */
    @Override
    public MonetaryAmount nullSafeGet(ResultSet resultSet, int position, SharedSessionContractImplementor session, Object owner)
            throws SQLException {
        String resultSetString = resultSet.getString(position);
        return MonetaryAmount.fromString(resultSetString);
    }

    /*
     Called when a MonetaryAmount value has to be stored in the database. We convert the value to the target currency,
     then set the amount and currency on the provided PreparedStatement (Unless the MonetaryAmount was null, in that
     case, we call setNull() to prepare the statement.)
     */
    @Override
    public void nullSafeSet(PreparedStatement statement, MonetaryAmount value, int index, SharedSessionContractImplementor session)
            throws SQLException {

        if (Objects.isNull(value)) {
            statement.setNull(index + 1, StandardBasicTypes.STRING.getSqlTypeCode());
            return;
        }
        MonetaryAmount dbAmount = convert(value, convertTo);
        statement.setString(index, dbAmount.toString());

    }

    /*
     We can implement whatever currency conversion routine we need. You'll have to implement this
     code with a real currency converter in a real application.
     */
    private MonetaryAmount convert(MonetaryAmount amount, Currency convertTo) {
        return new MonetaryAmount(amount.value().multiply(CURRENCY_USD_CONSTANT), convertTo);
    }

    /*
     If Hibernate has to make a copy of the value, it will call this method. For simple immutable classes like
     MonetaryAmount, we can return the given instance.
     */
    @Override
    public MonetaryAmount deepCopy(MonetaryAmount value) {
        return value;
    }

    /*
     Hibernate can enable some optimizations if it knows that MonetaryAmount is immutable.
     */
    @Override
    public boolean isMutable() {
        return false;
    }

    /*
     Hibernate calls disassemble when it stores a value in the global shared second-level cache. We need to return a
     Serializable representation. For MonetaryAmount, a String representation is an easy solution. Or,
     because MonetaryAmount is actually Serializable, we could return it directly.
     */
    @Override
    public Serializable disassemble(MonetaryAmount value) {
        return value.toString();
    }

    /*
     Hibernate calls this method when it reads the serialized representation from the global shared second-level
     cache. We create a MonetaryAmount instance from the String
     representation. Or, if we have stored a serialized MonetaryAmount, we could return it directly.
     */
    @Override
    public MonetaryAmount assemble(Serializable cached, Object owner) {
        return MonetaryAmount.fromString((String) cached);
    }

    /*
     Called during EntityManager#merge() operations, we need to return a copy of the original. Or, if
     the value type is immutable, like MonetaryAmount, we can simply return the original.
     */
    @Override
    public MonetaryAmount replace(MonetaryAmount detached, MonetaryAmount managed, Object owner) {
        return null;
    }

    @Override
    public boolean equals(MonetaryAmount x, MonetaryAmount y) {
        return x == y || !(x == null || y == null) && x.equals(y);
    }

    @Override
    public int hashCode(MonetaryAmount x) {
        return x.hashCode();
    }

    @Override
    public void setParameterValues(Properties parameters) {
        String convertToParameter = parameters.getProperty("convertTo");
        this.convertTo = Currency.getInstance(
                convertToParameter != null ? convertToParameter : "USD"
        );
    }
}
