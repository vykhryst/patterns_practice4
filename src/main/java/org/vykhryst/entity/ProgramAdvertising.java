package org.vykhryst.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramAdvertising {
    private long advertisingId;
    private int quantity;
    private String name;
    private String measurement;
    private BigDecimal unitPrice;
    private String description;

    public ProgramAdvertising(Builder builder) {
        this.advertisingId = builder.advertisingId;
        this.quantity = builder.quantity;
        this.name = builder.name;
        this.measurement = builder.measurement;
        this.unitPrice = builder.unitPrice;
        this.description = builder.description;
    }
    @NoArgsConstructor
    public static class Builder {
        private long advertisingId;
        private int quantity;
        private String name;
        private String measurement;
        private BigDecimal unitPrice;
        private String description;

        public Builder(Advertising advertising) {
            this.advertisingId = advertising.getId();
            this.name = advertising.getName();
            this.measurement = advertising.getMeasurement();
            this.unitPrice = advertising.getUnitPrice();
            this.description = advertising.getDescription();
        }

        public Builder(ProgramAdvertising programAdvertising) {
            this.advertisingId = programAdvertising.getAdvertisingId();
            this.quantity = programAdvertising.getQuantity();
            this.name = programAdvertising.getName();
            this.measurement = programAdvertising.getMeasurement();
            this.unitPrice = programAdvertising.getUnitPrice();
            this.description = programAdvertising.getDescription();
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder advertisingId(long advertisingId) {
            this.advertisingId = advertisingId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder measurement(String measurement) {
            this.measurement = measurement;
            return this;
        }

        public Builder unitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public ProgramAdvertising build() {
            return new ProgramAdvertising(this);
        }
    }
}
