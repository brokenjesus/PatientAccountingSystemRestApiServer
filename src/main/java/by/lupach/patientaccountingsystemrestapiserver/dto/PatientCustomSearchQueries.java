package by.lupach.patientaccountingsystemrestapiserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientCustomSearchQueries {
    private String patientName;
    private Integer age;
    private String wardNumber;
    private String phone;
    private Date admissionDate;
    private Date transferDate;
}
