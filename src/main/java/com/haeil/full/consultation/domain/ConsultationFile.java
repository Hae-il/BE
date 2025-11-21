package com.haeil.full.consultation.domain;

import com.haeil.full.file.domain.FileEntity;
import com.haeil.full.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "consultation_file")
@Entity
@Getter
@NoArgsConstructor
public class ConsultationFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private FileEntity file;

    @Column(name = "description")
    private String description;

    @Builder
    public ConsultationFile(Consultation consultation, FileEntity file, String description) {
        this.consultation = consultation;
        this.file = file;
        this.description = description;
    }

    public String getFileName() {
        return file.getOriginalFilename();
    }

    public String getFormattedSize() {
        long size = file.getFileSize();
        if (size < 1024) return size + " B";
        int z = (63 - Long.numberOfLeadingZeros(size)) / 10;
        return String.format("%.1f %sB", (double)size / (1L << (z*10)), " KMGTPE".charAt(z));
    }

    public String getIconClass() {
        String extension = "";
        int i = file.getOriginalFilename().lastIndexOf('.');
        if (i > 0) {
            extension = file.getOriginalFilename().substring(i+1).toLowerCase();
        }
        
        switch (extension) {
            case "pdf": return "ti-file-type-pdf";
            case "doc": case "docx": return "ti-file-type-doc";
            case "xls": case "xlsx": return "ti-file-type-xls";
            case "ppt": case "pptx": return "ti-file-type-ppt";
            case "zip": case "rar": return "ti-file-zip";
            case "txt": return "ti-file-text";
            case "jpg": case "jpeg": case "png": case "gif": return "ti-photo";
            default: return "ti-file";
        }
    }
}
