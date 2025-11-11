package ru.Diana.service;

import ru.Diana.entity.Report;

import java.util.concurrent.CompletableFuture;

public interface ReportService {
    CompletableFuture<Long> createReportAsync();
    String getReportContent(Long reportId);
    Long createReport();
}