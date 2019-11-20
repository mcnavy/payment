package afanas.service.controller;


import afanas.service.domain.Payment;
import afanas.service.repo.PaymentRepo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@Api(value = "API Description")
public class SomeController {
   private final PaymentRepo paymentRepo;

    public SomeController(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    @ApiOperation("returns list of all payments")
    @GetMapping("/all")
    public List<Payment> list(){
        return paymentRepo.findAll();
    }

    @ApiOperation("returns payment by order id")
    @GetMapping("/byOrderID/{orderID}")
    public Payment getPaymentByOrderID(@PathVariable int orderID){
        return paymentRepo.findByOrderID(orderID);
    }

    @ApiOperation("Creates new payment")
    @PostMapping("/create")
    public Payment create(@RequestBody Payment payment){
        return paymentRepo.save(
                new Payment(payment.getOrderID(), payment.getStatus())
        );
    }

    @ApiOperation("Updates info on payment")
    @PutMapping("/update/{id}")
    public Payment update(
            @PathVariable("id") Payment paymentFromDb,
            @RequestBody Payment payment){
        BeanUtils.copyProperties(payment,paymentFromDb,"id");
        return paymentRepo.save(payment);
    }

    @ApiOperation("Cancel payment by Id")
    @PutMapping("/cancel/{orderid}")
    public Payment cancel (
            @PathVariable("orderid") int orderID) {
        Payment payment = paymentRepo.findByOrderID(orderID);
        payment.setStatus("Canceled");
        return paymentRepo.save(payment);
    }
}
