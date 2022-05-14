package io.github.leomoreiradev.quarkussocial.domain.repository;

import io.github.leomoreiradev.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped // cria uma instancia de UserRepository dentro com contexto da aplicaçao (Singleton)
public class UserRepository  implements PanacheRepository<User> {
}
