package aviationModelling.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "pilot")
public class Pilot {

    @Id
    @Column(name = "pilot_id")
    private Integer id;

    @Column(name = "pilot_bib")
    private String pilotBib;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "pilot_class")
    private String pilotClass;

    private String ama;
    private String fai;

    @Column(name = "fai_license")
    private String faiLicence;

    @Column(name = "team_name")
    private String teamName;







}
