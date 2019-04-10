package com.code.pojoenum;

enum AmStaff {
    firstName("first_name"),
    lastName("last_name"),
    englishName("english_name"),
    email("email"),
    businessNo("business_no"),
    jobTitle("job_title"),
    employmentDate("employment_date"),
    resignationDate("resignation_date"),
    skype("skype"),
    gender("gender"),
    tel("tel");

    private String value;

    AmStaff(String value) {
        this.value=value;
    }

    public void setValue(String value) {
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}