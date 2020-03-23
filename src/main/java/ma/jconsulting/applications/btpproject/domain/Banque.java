package ma.jconsulting.applications.btpproject.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Banque.
 */
@Entity
@Table(name = "banque")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "banque")
public class Banque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "banque", nullable = false)
    private String banque;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_tel")
    private String contactTel;

    @Column(name = "adresse_agence")
    private String adresseAgence;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBanque() {
        return banque;
    }

    public Banque banque(String banque) {
        this.banque = banque;
        return this;
    }

    public void setBanque(String banque) {
        this.banque = banque;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public Banque contactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactTel() {
        return contactTel;
    }

    public Banque contactTel(String contactTel) {
        this.contactTel = contactTel;
        return this;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getAdresseAgence() {
        return adresseAgence;
    }

    public Banque adresseAgence(String adresseAgence) {
        this.adresseAgence = adresseAgence;
        return this;
    }

    public void setAdresseAgence(String adresseAgence) {
        this.adresseAgence = adresseAgence;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Banque)) {
            return false;
        }
        return id != null && id.equals(((Banque) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Banque{" +
            "id=" + getId() +
            ", banque='" + getBanque() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            ", contactTel='" + getContactTel() + "'" +
            ", adresseAgence='" + getAdresseAgence() + "'" +
            "}";
    }
}
