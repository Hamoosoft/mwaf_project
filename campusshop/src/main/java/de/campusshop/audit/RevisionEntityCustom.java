package de.campusshop.audit;

import jakarta.persistence.*;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

/**
 * Eigene Revision-Entity für Hibernate Envers.
 *
 * Wird benutzt, um Änderungen an Daten zu versionieren.
 * allocationSize = 1 passt zur DB-Sequence (incrementBy = 1).
 */
@Entity
@Table(name = "revinfo")
@RevisionEntity
public class RevisionEntityCustom {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "revinfo_seq_gen")
    @SequenceGenerator(
            name = "revinfo_seq_gen",
            sequenceName = "revinfo_seq",
            allocationSize = 1
    )
    @RevisionNumber
    @Column(name = "rev", nullable = false)
    private int rev;

    @RevisionTimestamp
    @Column(name = "revtstmp")
    private long revisionTimestamp;

    public int getRev() {
        return rev;
    }

    public long getRevisionTimestamp() {
        return revisionTimestamp;
    }
}
