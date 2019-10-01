package afanas.service.controller;


import afanas.service.domain.Payment;
import afanas.service.repo.PaymentRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class SomeController {
   private final PaymentRepo paymentRepo;

    public SomeController(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    @GetMapping
    public List<Payment> list(){
        return paymentRepo.findAll();
    }
    @GetMapping("{id}")
    public Payment getOne(@PathVariable("id") Payment payment){
        return payment;
    }



    @PostMapping
    public Payment create(@RequestBody Payment payment){
        return paymentRepo.save(payment);
    }
    @PutMapping("{id}")
    public Payment update(
            @PathVariable("id") Payment paymentFromDb,
            @RequestBody Payment payment){
        BeanUtils.copyProperties(payment,paymentFromDb,"id");
        return paymentRepo.save(payment);

    }
}
