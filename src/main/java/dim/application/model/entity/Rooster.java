package dim.application.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "rooster_list")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rooster {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rooster_list_seq_gen")
    @SequenceGenerator(name = "rooster_list_seq_gen", sequenceName = "rooster_list_seq", allocationSize = 1)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "month_count")
    private Long monthCount;

    public Rooster(String login, String fullName) {
        this.login = login;
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Rooster rooster = (Rooster) o;
        return id != null && Objects.equals(id, rooster.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
