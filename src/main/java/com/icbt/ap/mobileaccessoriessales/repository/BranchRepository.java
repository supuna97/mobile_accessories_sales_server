package com.icbt.ap.mobileaccessoriessales.repository;

import com.icbt.ap.mobileaccessoriessales.entity.Branch;
import com.icbt.ap.mobileaccessoriessales.repository.main.CrudRepository;

public interface BranchRepository extends CrudRepository<String, Branch> {

    Branch findByName(String name);

    Branch findByTel(String tel);
}
