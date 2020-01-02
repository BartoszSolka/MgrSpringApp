package com.solka.application.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsvPlayer {

    @CsvBindByName(column = "full_name")
    private String fullName;

    @CsvBindByName
    private String position;

    @CsvBindByName(column = "Club")
    private String club;
}
