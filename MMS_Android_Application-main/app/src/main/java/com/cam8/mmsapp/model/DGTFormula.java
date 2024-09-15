package com.cam8.mmsapp.model;

public class DGTFormula {
    int id = 0;
    String name = "";
    String formula = "";

    public DGTFormula(int id, String name, String formula) {
        this.id = id;
        this.name = name;
        this.formula = formula;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getFormula() { return formula; }
    public void setFormula(String value) { this.formula = value; }

    @Override
    public String toString() {
        return name;
    }
}
