package com.github.kerner1000.terra.commons;

public enum Coin implements Contract {
    UST {
        @Override
        public String getContract() {
            return "<native>";
        }
    }, LUNA {
        @Override
        public String getContract() {
            return "<native>";
        }
    }, MARS {
        public String getContract() {
            return "terra12hgwnpupflfpuual532wgrxu2gjp0tcagzgx4n";
        }
    }
}
