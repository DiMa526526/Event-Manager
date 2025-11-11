package ru.Diana.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.Diana.entity.Report;
import ru.Diana.entity.ReportStatus;

import java.util.List;

@Repository
@RepositoryRestResource
public interface ReportRepository extends CrudRepository<Report, Long> {
    List<Report> findByStatus(ReportStatus status);
    List<Report> findByReportType(String reportType);
}
