package com.example.customerservice;

import com.example.customerservice.entities.Customer;
import com.example.customerservice.repo.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
    @Bean
public CommandLineRunner commandLineRunner(CustomerRepository customerRepository){
return args -> {
    customerRepository.save(Customer.builder()
                    .name("Nephthali")
                    .email("thali@isga.ma")

            .build());
    customerRepository.save(Customer.builder()
            .name("Thali")
            .email("thalituto@isga.ma")

            .build());
    customerRepository.save(Customer.builder()
            .name("Tutondele")
            .email("tutondele@isga.ma")

            .build());
};
}
}
