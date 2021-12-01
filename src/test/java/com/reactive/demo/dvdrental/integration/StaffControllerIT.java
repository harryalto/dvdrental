package com.reactive.demo.dvdrental.integration;

import com.reactive.demo.dvdrental.api.model.StaffCoreModel;
import com.reactive.demo.dvdrental.api.model.StaffModel;
import com.reactive.demo.dvdrental.data.entity.Staff;
import com.reactive.demo.dvdrental.data.repository.StaffRepository;
import com.reactive.demo.dvdrental.service.StaffServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import(StaffServiceImpl.class)
public class StaffControllerIT {

    @MockBean
    private StaffRepository staffRepositoryMock;

    @Autowired
    private WebTestClient testClient;

    private Staff staffSample = Staff.builder().staffId(1L).firstName("John").lastName("Doe").username("something").
            email("aa@gmail.com").build();
    private StaffCoreModel staffCoreModel = StaffCoreModel.builder().firstName("John").lastName("Doe").username("something").
            email("aa@gmail.com").build();
    private StaffModel staffResponse = StaffModel.builder().staffId(1L).firstName("John").lastName("Doe").username("something").
            email("aa@gmail.com").build();


    @BeforeEach
    public void setUp() {
        BDDMockito.when(staffRepositoryMock.findFirstByStaffId(1L))
                .thenReturn(Flux.just(staffSample));
    }


    @Test
    @DisplayName("Get Staff resource by Staff Id")
    public void getStaffById_ReturnMonoOfStaff_WhenSuccessful() {
        testClient
                .get()
                .uri("/v1/staff/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.staffId").isEqualTo(staffSample.getStaffId())
                .jsonPath("$.firstName").isEqualTo(staffSample.getFirstName());
    }
}
