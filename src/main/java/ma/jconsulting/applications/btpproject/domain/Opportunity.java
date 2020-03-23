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

/**
 * A Opportunity.
 */
@Entity
@Table(name = "opportunity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "opportunity")
public class Opportunity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "numero_appel_offre", nullable = false)
    private String numeroAppelOffre;

    @Column(name = "date_remise_plis")
    private LocalDate dateRemisePlis;

    @Column(name = "montant_caution")
    private Double montantCaution;

    @Column(name = "objet_affaire")
    private String objetAffaire;

    @Column(name = "estimation_budget")
    private Double estimationBudget;

    @Column(name = "commentaires")
    private String commentaires;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("opportunities")
    private MaitreOuvrage maitreOuvrage;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Caution caution;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroAppelOffre() {
        return numeroAppelOffre;
    }

    public Opportunity numeroAppelOffre(String numeroAppelOffre) {
        this.numeroAppelOffre = numeroAppelOffre;
        return this;
    }

    public void setNumeroAppelOffre(String numeroAppelOffre) {
        this.numeroAppelOffre = numeroAppelOffre;
    }

    public LocalDate getDateRemisePlis() {
        return dateRemisePlis;
    }

    public Opportunity dateRemisePlis(LocalDate dateRemisePlis) {
        this.dateRemisePlis = dateRemisePlis;
        return this;
    }

    public void setDateRemisePlis(LocalDate dateRemisePlis) {
        this.dateRemisePlis = dateRemisePlis;
    }

    public Double getMontantCaution() {
        return montantCaution;
    }

    public Opportunity montantCaution(Double montantCaution) {
        this.montantCaution = montantCaution;
        return this;
    }

    public void setMontantCaution(Double montantCaution) {
        this.montantCaution = montantCaution;
    }

    public String getObjetAffaire() {
        return objetAffaire;
    }

    public Opportunity objetAffaire(String objetAffaire) {
        this.objetAffaire = objetAffaire;
        return this;
    }

    public void setObjetAffaire(String objetAffaire) {
        this.objetAffaire = objetAffaire;
    }

    public Double getEstimationBudget() {
        return estimationBudget;
    }

    public Opportunity estimationBudget(Double estimationBudget) {
        this.estimationBudget = estimationBudget;
        return this;
    }

    public void setEstimationBudget(Double estimationBudget) {
        this.estimationBudget = estimationBudget;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public Opportunity commentaires(String commentaires) {
        this.commentaires = commentaires;
        return this;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public MaitreOuvrage getMaitreOuvrage() {
        return maitreOuvrage;
    }

    public Opportunity maitreOuvrage(MaitreOuvrage maitreOuvrage) {
        this.maitreOuvrage = maitreOuvrage;
        return this;
    }

    public void setMaitreOuvrage(MaitreOuvrage maitreOuvrage) {
        this.maitreOuvrage = maitreOuvrage;
    }

    public Caution getCaution() {
        return caution;
    }

    public Opportunity caution(Caution caution) {
        this.caution = caution;
        return this;
    }

    public void setCaution(Caution caution) {
        this.caution = caution;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Opportunity)) {
            return false;
        }
        return id != null && id.equals(((Opportunity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Opportunity{" +
            "id=" + getId() +
            ", numeroAppelOffre='" + getNumeroAppelOffre() + "'" +
            ", dateRemisePlis='" + getDateRemisePlis() + "'" +
            ", montantCaution=" + getMontantCaution() +
            ", objetAffaire='" + getObjetAffaire() + "'" +
            ", estimationBudget=" + getEstimationBudget() +
            ", commentaires='" + getCommentaires() + "'" +
            "}";
    }
}
