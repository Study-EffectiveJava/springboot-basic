package prgms.vouchermanagementapp.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import prgms.vouchermanagementapp.customer.Customer;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class Customers {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public Customers(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("customer")
                .usingGeneratedKeyColumns("id")
                .usingColumns("customer_name");
    }

    public Customer save(Customer customer) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(customer);
        Number key = jdbcInsert.executeAndReturnKey(parameterSource);
        return new Customer(key.longValue(), customer.getCustomerName());
    }

    public Optional<Customer> findByName(String name) {
        String sql = "select id, customer_name from customer " +
                "where customer_name=:customerName";

        try {
            Map<String, Object> param = Map.of("customerName", name);
            Customer customer = template.queryForObject(sql, param, itemRowMapper());
            return Optional.of(Objects.requireNonNull(customer));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Customer> itemRowMapper() {
        return BeanPropertyRowMapper.newInstance(Customer.class);
    }
}
