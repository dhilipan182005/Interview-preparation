package t.com.onlinebooking.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import t.com.onlinebooking.DHO.Report;
import t.com.onlinebooking.Repository.ReportRepo;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepo reportRepo;

    public List<Report> getAllReports(){
        return reportRepo.findAll();
    }

    public String addReport(Report report){
        report.setReportDate(LocalDate.now());
        reportRepo.save(report);
        return "Report added successfully";
    }
}