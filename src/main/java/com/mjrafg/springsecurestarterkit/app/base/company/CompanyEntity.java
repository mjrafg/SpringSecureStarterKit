package com.mjrafg.springsecurestarterkit.app.base.company;

import com.mjrafg.springsecurestarterkit.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Description("품목코드")
public class CompanyEntity extends BaseEntity {

    @NotBlank
    @Size(max = 20, min = 5)
    private String code;

    @NotBlank
    @Size(max = 50, min = 3)
    private String name;

    @Enumerated(EnumType.STRING)
    private CompanyType type;

    @Size(min=12,max = 12)
    private String registrationNumber;

    @Size(max = 50)
    private String bossName;

    @Size(max = 30)
    private String phone;

    @Size(max = 30)
    private String fax;


    @Size(max = 50)
    @Email
    private String email;

    @Size(max = 50)
    private String managerName;

    @Size(max = 30)
    private String managerMobile;

    @Description("주소")
    @Size(max = 100)
    private String address;

    @Description("상세주소")
    @Size(max = 100)
    private String addressDetail;
}


