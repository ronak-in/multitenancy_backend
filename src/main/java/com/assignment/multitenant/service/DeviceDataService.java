package com.assignment.multitenant.service;

import com.assignment.multitenant.bean.DeviceData;
import com.assignment.multitenant.repo.DeviceDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class DeviceDataService {

    @Autowired
    private DeviceDataRepository deviceDataRepository;

    public void save(DeviceData deviceData) {
        deviceDataRepository.save(deviceData);
    }

    public List<DeviceData> getAll() throws SQLException {
        return deviceDataRepository.findAll();

    }

    public DeviceData get(Long id) {
        return deviceDataRepository.findById(id).get();
    }

    public DeviceData getByName(String name) {
        return deviceDataRepository.findByTenantId(name);
    }

}
