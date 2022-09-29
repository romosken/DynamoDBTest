package br.com.test.dynamo.dynamodbtest.repository;

import br.com.test.dynamo.dynamodbtest.model.Task;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@EnableScan
public interface TaskRepository extends CrudRepository<Task, UUID> {
}
