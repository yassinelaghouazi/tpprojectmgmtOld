package ma.jconsulting.applications.btpproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

import ma.jconsulting.applications.btpproject.domain.enumeration.TypeCaution;

import ma.jconsulting.applications.btpproject.domain.enumeration.StatusCaution;

/**
 * A Caution.
 */
@Entity
@Table(name = "caution")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "caution")
public class Caution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_caution")
    private String numeroCaution;

    @Column(name = "numero_appel_offre")
    private String numeroAppelOffre;

    @Column(name = "numero_marche")
    private String numeroMarche;

    @Column(name = "objet_caution")
    private String objetCaution;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_caution")
    private TypeCaution typeCaution;

    @Column(name = "montant_caution")
    private Double montantCaution;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_caution")
    private StatusCaution statusCaution;

    @Column(name = "date_demande")
    private LocalDate dateDemande;

    @Column(name = "date_retrait")
    private LocalDate dateRetrait;

    @Column(name = "date_depot")
    private LocalDate dateDepot;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("cautions")
    private Banque banque;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("cautions")
    private MaitreOuvrage maitreOuvrage;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroCaution() {
        return numeroCaution;
    }

    public Caution numeroCaution(String numeroCaution) {
        this.numeroCaution = numeroCaution;
        return this;
    }

    public void setNumeroCaution(String numeroCaution) {
        this.numeroCaution = numeroCaution;
    }

    public String getNumeroAppelOffre() {
        return numeroAppelOffre;
    }

    public Caution numeroAppelOffre(String numeroAppelOffre) {
        this.numeroAppelOffre = numeroAppelOffre;
        return this;
    }

    public void setNumeroAppelOffre(String numeroAppelOffre) {
        this.numeroAppelOffre = numeroAppelOffre;
    }

    public String getNumeroMarche() {
        return numeroMarche;
    }

    public Caution numeroMarche(String numeroMarche) {
        this.numeroMarche = numeroMarche;
        return this;
    }

    public void setNumeroMarche(String numeroMarche) {
        this.numeroMarche = numeroMarche;
    }

    public String getObjetCaution() {
        return objetCaution;
    }

    public Caution objetCaution(String objetCaution) {
        this.objetCaution = objetCaution;
        return this;
    }

    public void setObjetCaution(String objetCaution) {
        this.objetCaution = objetCaution;
    }

    public TypeCaution getTypeCaution() {
        return typeCaution;
    }

    public Caution typeCaution(TypeCaution typeCaution) {
        this.typeCaution = typeCaution;
        return this;
    }

    public void setTypeCaution(TypeCaution typeCaution) {
        this.typeCaution = typeCaution;
    }

    public Double getMontantCaution() {
        return montantCaution;
    }

    public Caution montantCaution(Double montantCaution) {
        this.montantCaution = montantCaution;
        return this;
    }

    public void setMontantCaution(Double montantCaution) {
        this.montantCaution = montantCaution;
    }

    public StatusCaution getStatusCaution() {
        return statusCaution;
    }

    public Caution statusCaution(StatusCaution statusCaution) {
        this.statusCaution = statusCaution;
        return this;
    }

    public void setStatusCaution(StatusCaution statusCaution) {
        this.statusCaution = statusCaution;
    }

    public LocalDate getDateDemande() {
        return dateDemande;
    }

    public Caution dateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
        return this;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    public LocalDate getDateRetrait() {
        return dateRetrait;
    }

    public Caution dateRetrait(LocalDate dateRetrait) {
        this.dateRetrait = dateRetrait;
        return this;
    }

    public void setDateRetrait(LocalDate dateRetrait) {
        this.dateRetrait = dateRetrait;
    }

    public LocalDate getDateDepot() {
        return dateDepot;
    }

    public Caution dateDepot(LocalDate dateDepot) {
        this.dateDepot = dateDepot;
        return this;
    }

    public void setDateDepot(LocalDate dateDepot) {
        this.dateDepot = dateDepot;
    }

    public Banque getBanque() {
        return banque;
    }

    public Caution banque(Banque banque) {
        this.banque = banque;
        return this;
    }

    public void setBanque(Banque banque) {
        this.banque = banque;
    }

    public MaitreOuvrage getMaitreOuvrage() {
        return maitreOuvrage;
    }

    public Caution maitreOuvrage(MaitreOuvrage maitreOuvrage) {
        this.maitreOuvrage = maitreOuvrage;
        return this;
    }

    public void setMaitreOuvrage(MaitreOuvrage maitreOuvrage) {
        this.maitreOuvrage = maitreOuvrage;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Caution)) {
            return false;
        }
        return id != null && id.equals(((Caution) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Caution{" +
            "id=" + getId() +
            ", numeroCaution='" + getNumeroCaution() + "'" +
            ", numeroAppelOffre='" + getNumeroAppelOffre() + "'" +
            ", numeroMarche='" + getNumeroMarche() + "'" +
            ", objetCaution='" + getObjetCaution() + "'" +
            ", typeCaution='" + getTypeCaution() + "'" +
            ", montantCaution=" + getMontantCaution() +
            ", statusCaution='" + getStatusCaution() + "'" +
            ", dateDemande='" + getDateDemande() + "'" +
            ", dateRetrait='" + getDateRetrait() + "'" +
            ", dateDepot='" + getDateDepot() + "'" +
            "}";
    }
}
