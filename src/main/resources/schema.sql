CREATE TABLE payment_method (
                                id BIGINT GENERATED BY DEFAULT AS IDENTITY,
                                name VARCHAR(255) NOT NULL UNIQUE,
                                display_name VARCHAR(255) NOT NULL,
                                payment_type VARCHAR(255) NOT NULL,
                                PRIMARY KEY (id)
);

CREATE TABLE payment_plan (
                              id BIGINT GENERATED BY DEFAULT AS IDENTITY,
                              net_amount DECIMAL(10, 2) NOT NULL,
                              tax_amount DECIMAL(10, 2) NOT NULL,
                              gross_amount DECIMAL(10, 2) NOT NULL,
                              currency VARCHAR(3) NOT NULL,
                              duration VARCHAR(255) NOT NULL,
                              payment_method_id BIGINT NOT NULL,
                              PRIMARY KEY (id),
                              FOREIGN KEY (payment_method_id) REFERENCES payment_method(id)
);