package com.github.kerner1000.terra.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Hans {

    private final List<Integer> owners;  //represents number of dogs per owner

    private final List<Integer> values;
    private final int max;

    public Hans(Collection<? extends Integer> owners, Collection<? extends Integer> values, int max) {
        this.owners = new ArrayList<>(owners);
        this.values = new ArrayList<>(values);
        this.max = max;
    }

    public String printHistrogram(int bin) {

        //todo add check to make sure bin < MAX_DOGS_NUM
        List<Column> columns = makeColumns(bin);
        calcColumnsData(columns);
        normalizeColumnsData(columns);
        columns = columns.stream().filter(c -> c.getQty() > 0).collect(Collectors.toList());
        return new Histogram(columns).toString();
    }

    //construct all columns, set interval ends
    private List<Column> makeColumns(int bin) {

        List<Column> columns = new ArrayList<>();

        for (int i = 0; i <= max; i += bin) {
            int intervalEnd = (i + bin) > max ? max : (i + bin);
            columns.add(new Column(i, intervalEnd));
            i++;
        }
        return columns;
    }

    //calculate quantity of each column
    private void calcColumnsData(List<Column> columns) {

        for (Column col : columns) {
            //count the number of owners who has dogs within interval
            int ownersCounter = 0;
            for (int i = 0; i < owners.size(); i++) {
                var numberOfDogs = owners.get(i);
                if ((numberOfDogs >= col.getInteravalStart())
                        && (numberOfDogs <= col.getIntervalEnd())) {
                    double value = values.get(i);
                    ownersCounter += value;
                }
            }
            //update column quantity
            col.setQty(ownersCounter);
        }
        return;
    }

    private void normalizeColumnsData(List<Column> columns) {
        int min = columns.stream().mapToInt(Column::getQty).min().getAsInt();
        int max = columns.stream().mapToInt(Column::getQty).max().getAsInt();
        for (Column col : columns) {
            var normalized = normalize(col.getQty(), min, max);
            col.setQty((int) Math.round(normalized * 6));
        }
    }

    double normalize(double value, double min, double max) {
        if (value == min)
            return 0;
        return ((value - min) / (max - min));
    }

    class Column {
        //interval ends, quantity of owners
        int interavalStart, intervalEnd, qty = 0;

        Column(int interavalStart, int intervalEnd) {
            this.interavalStart = interavalStart;
            this.intervalEnd = intervalEnd;
        }

        int getQty() {
            return qty;
        }

        void setQty(int qty) {
            this.qty = qty;
        }

        int getInteravalStart() {
            return interavalStart;
        }

        int getIntervalEnd() {
            return intervalEnd;
        }

        @Override
        public String toString() {
            return interavalStart + "-" + intervalEnd + ": " + qty;
        }
    }

    class Histogram {

        private List<Column> columns; //histogram columns
        //representation of graph mark and space
        private static final String MARK = "*", SPACE = " ";
        private static final int COLUMN_WIDTH = 8;
        private int maxHeight = 0; //size of highest histogram
        //histogram data. each row contains makrs or space. last
        //row contains footer
        private String graphRepresentation[][];

        Histogram(List<Column> columns) {
            this.columns = new ArrayList<>(columns);
            calculateMaxHeight();
            prepareGraphRepresentation();
        }

        //find tallest column
        private void calculateMaxHeight() {
            for (Column col : columns) {
                if (col.getQty() > maxHeight) {
                    maxHeight = col.getQty();
                }
            }
            maxHeight += 1; //add 1 for column footer
        }

        //fill graphRepresentation with spaces, marks or footer
        private void prepareGraphRepresentation() {

            graphRepresentation = new String[maxHeight][columns.size()];

            for (int colIndex = 0; colIndex < columns.size(); colIndex++) {

                Column col = columns.get(colIndex);
                int rowCounter = 0;

                for (int rowIndex = maxHeight - 1; rowIndex >= 0; rowIndex--) {

                    String s = SPACE;
                    if (rowCounter == 0) { //histogram footer
                        s = col.getInteravalStart() + "-" + col.getIntervalEnd();
                    } else if (rowCounter <= col.getQty()) {
                        s = MARK;
                    }
                    graphRepresentation[rowIndex][colIndex] = format(s);
                    rowCounter++;
                }
            }
        }

        //add spaces to s to make it as wide as column width
        private String format(String s) {

            int leftSpaces = (COLUMN_WIDTH - s.length()) / 2;
            int rightSpaces = COLUMN_WIDTH - s.length() - leftSpaces;

            StringBuilder sb = new StringBuilder();
            //add left spaces
            for (int spaces = 0; spaces < leftSpaces; spaces++) {
                sb.append(SPACE);
            }
            sb.append(s);
            for (int spaces = 0; spaces < rightSpaces; spaces++) {
                sb.append(SPACE);
            }

            return sb.toString();
        }

        @Override
        public String toString() {

            StringBuilder sb = new StringBuilder();
            for (String[] row : graphRepresentation) {
                for (String s : row) {
                    sb.append(s);
                }
                sb.append("\n");
            }

            return sb.toString();
        }
    }
}

//represents a single histogram column


//represents histogram graph

