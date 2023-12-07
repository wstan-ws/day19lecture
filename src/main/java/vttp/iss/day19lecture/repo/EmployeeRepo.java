package vttp.iss.day19lecture.repo;

import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import vttp.iss.day19lecture.model.Employee;

@Repository
public class EmployeeRepo {

    private String hashRef = "employees";

    @Resource(name = "redisEmployeeTemplate")
    private HashOperations<String, String, Employee> hOps;

    // private ListOperations<String, Employee> lOps;

    public void saveRecord(Employee e) {
        hOps.put(hashRef, e.getEmployeeId().toString(), e);
    }
    
    public Employee getRecord(Integer id) {
        Employee e = hOps.get(hashRef, id.toString());
        return e;
    }

    public Map<String, Employee> getAll() {
        Map<String, Employee> map = hOps.entries(hashRef);
        return map;
    }
}
