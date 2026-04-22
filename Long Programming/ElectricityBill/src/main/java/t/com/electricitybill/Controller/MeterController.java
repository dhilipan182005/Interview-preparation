package t.com.electricitybill.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import t.com.electricitybill.DHO.Meter;
import t.com.electricitybill.Service.MeterService;
import java.util.List;
@RestController
@RequestMapping("/Meter")
public class MeterController {
    @Autowired
    private MeterService ms;
    @GetMapping("/Getting")
    public List<Meter> GetMeter(){
        return ms.GetMeter();
    }

    @PostMapping("/PostMeter")
    public Meter PostMeter(@RequestBody Meter m){
        return ms.PostMeter(m);
    }

    @PutMapping("/{id}")
    public Meter PutMeter(@PathVariable("id") int id,@RequestBody Meter m){

        return ms.PutMeter(m, id);
    }

    @DeleteMapping("/{id}")
    public String DeleteMeter(@PathVariable int id){
        return ms.DeleteMeter(id);
    }
}
