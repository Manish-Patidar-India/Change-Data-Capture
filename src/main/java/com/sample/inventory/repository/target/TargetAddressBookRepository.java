package com.sample.inventory.repository.target;

import com.sample.inventory.entity.target.TargetAddressBookEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetAddressBookRepository extends JpaRepository<TargetAddressBookEntry, Long> {
}