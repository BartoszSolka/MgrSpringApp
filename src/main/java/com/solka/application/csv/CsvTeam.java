package com.solka.application.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsvTeam {

    @CsvBindByName(column = "Club")
    private String name;
}
