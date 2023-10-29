package com.c195_software_ii__advanced_java_concepts_pa;

import com.c195_software_ii__advanced_java_concepts_pa.DAO.JDBC;

public class Main {
    public static void main(String[] args) {
        JDBC.openConnection();

        JDBC.closeConnection();
    }
}