package com.reactive.demo.dvdrental.service;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.model.StaffCoreModel;
import com.reactive.demo.dvdrental.data.entity.Staff;
import com.reactive.demo.dvdrental.data.mapper.GenericMapper;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class StaffServiceImplTest {

    @InjectMocks
    private StaffServiceImpl staffService;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private GenericMapper genericMapper;

    private Staff staffSample = Staff.builder().staffId(1).firstName("John").lastName("Doe").username("something").
            email("aa@gmail.com").build();
    private StaffCoreModel staffCoreModel = StaffCoreModel.builder().firstName("John").lastName("Doe").username("something").
            email("aa@gmail.com").build();

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

    @Test
    @DisplayName("Create new staff resource")
    public void testCreateStaff_Successful() {
        BDDMockito.when(staffRepository.findFirstByFirstNameAndLastNameAndEmailAndUsername(ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());
        BDDMockito.when(genericMapper.convertToStaff(ArgumentMatchers.any(StaffCoreModel.class)))
                .thenReturn(staffSample);
        BDDMockito.when(staffRepository.save(ArgumentMatchers.any(Staff.class)))
                .thenReturn(Mono.just(staffSample));

        StepVerifier.create(staffService.save(staffCoreModel))
                .expectSubscription()
                .expectNext(Pair.of(false, staffSample))
                .verifyComplete();

    }


}