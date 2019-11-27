package afanas.service.controller;


import afanas.service.domain.Payment;
import afanas.service.repo.PaymentRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.util.List;
@EnableRabbit
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
    @GetMapping("/byOrderID/{orderid}")
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

    @RabbitListener(queues = "hello")
    public void cancelPaymentByOrderId(byte [] message) throws IOException {

        //String orderIdString = new String(message.getBody());
        //int orderId = Integer.parseInt(orderIdString);

        //Payment payment = paymentRepo.findByOrderID(orderId);
        //payment.setStatus("Canceled");
        String andrey = new String(message);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(andrey);
        int id = actualObj.get("order_id").asInt();
        String new_status = actualObj.get("status").asText();
        Payment payment = paymentRepo.findByOrderID(id);
        payment.setStatus(new_status);
        paymentRepo.save(payment);
        System.out.println(actualObj.get("status"));
    }
}
