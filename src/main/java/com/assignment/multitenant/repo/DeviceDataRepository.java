package com.assignment.multitenant.repo;

import com.assignment.multitenant.bean.DeviceData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceDataRepository extends JpaRepository<DeviceData, Long> {

    DeviceData findByTenantId(String name);

}
