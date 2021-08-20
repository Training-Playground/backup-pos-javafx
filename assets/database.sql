CREATE TABLE customer
(
    id      VARCHAR(8) PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    address VARCHAR(500) NOT NULL
);

CREATE TABLE item
(
    code        VARCHAR(10) PRIMARY KEY,
    description VARCHAR(100) NOT NULL,
    unit_price  DECIMAL      NOT NULL,
    qty_on_hand INT          NOT NULL
);