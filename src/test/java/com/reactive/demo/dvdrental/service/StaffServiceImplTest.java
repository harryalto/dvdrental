package com.reactive.demo.dvdrental.service;

import com.reactive.demo.dvdrental.data.entity.Staff;
import com.reactive.demo.dvdrental.data.repository.StaffRepository;
import com.reactive.demo.dvdrental.exception.DataNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class StaffServiceImplTest {

    @InjectMocks
    private StaffServiceImpl staffService;

    @Mock
    private StaffRepository staffRepository;
    private Staff staffSample = Staff.builder().staffId(1).firstName("John").lastName("Doe").build();

    @BeforeAll
    public static void blackHoundSetup() {
        BlockHound.install();
    }


    @Test
    @DisplayName("Get by Id when there is data")
    public void testGetById_Successful() {
        BDDMockito.when(staffRepository.findFirstByStaffId(ArgumentMatchers.anyLong()))
                .thenReturn(Flux.just(staffSample));
        StepVerifier.create(staffService.findById(1L))
                .expectSubscription()
                .expectNext(staffSample)
                .verifyComplete();

    }

    @Test
    @DisplayName("Get by Id when there is no data")
    public void testGetById_WhenEmpty() {
        BDDMockito.when(staffRepository.findFirstByStaffId(ArgumentMatchers.anyLong()))
                .thenReturn(Flux.empty());
        StepVerifier.create(staffService.findById(1L))
                .expectSubscription()
                .expectError(DataNotFoundException.class)
                .verify();

    }


}