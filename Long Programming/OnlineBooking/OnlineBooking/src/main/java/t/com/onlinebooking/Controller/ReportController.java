package t.com.onlinebooking.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import t.com.onlinebooking.DHO.Report;
import t.com.onlinebooking.Service.ReportService;
import java.util.List;

@RestController
@RequestMapping("/Report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/all")
    public List<Report> getAllReports(){
        return reportService.getAllReports();
    }

    @PostMapping("/add")
    public String addReport(@RequestBody Report report){
        return reportService.addReport(report);
    }
}