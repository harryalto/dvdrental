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
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        BDDMockito.when(staffRepositoryMock.findAll())
                .thenReturn(Flux.just(staffSample));
        BDDMockito.when(staffRepositoryMock.save(
                        ArgumentMatchers.any(Staff.class)))
                .thenReturn(Mono.just(staffSample));
        BDDMockito.when(staffRepositoryMock.findFirstByFirstNameAndLastNameAndEmailAndUsername(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("Get All Staff ")
    public void getAllStaff_ReturnFluxOfStaff_Successful() {
        testClient
                .get()
                .uri("/v1/staff")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.[0].staffId").isEqualTo(staffSample.getStaffId())
                .jsonPath("$.[0].firstName").isEqualTo(staffSample.getFirstName());
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

    @Test
    @DisplayName("Create Staff Resource")
    public void createStaff_ReturnMono_WhenSuccessful() {
        testClient.post().uri("/v1/staff")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(staffCoreModel), StaffCoreModel.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("John");
    }

    @Test
    @DisplayName("Delete Staff Resource by Id")
    public void deleteStaff_ReturnMono_WhenSuccessful() {
        BDDMockito.when(staffRepositoryMock.findById(1L))
                .thenReturn(Mono.just(staffSample));
        BDDMockito.when(staffRepositoryMock.delete(
                        ArgumentMatchers.any(Staff.class)))
                .thenReturn(Mono.empty());
        testClient
                .delete()
                .uri("/v1/staff/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
