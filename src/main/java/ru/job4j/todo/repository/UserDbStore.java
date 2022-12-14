package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
public class UserDbStore {
    private final SessionFactory sf;

    public UserDbStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Optional<User> create(User user) {
        Session session = sf.openSession();
        session.getTransaction();
        session.save(user);
        session.close();
        return Optional.ofNullable(user);
    }

    public Optional<User> findByEmail(String email) {
        Session session = sf.openSession();
        session.getTransaction();
        User user = session.createQuery("from ru.job4j.todo.model.User u where u.email = :fEmail", User.class)
                .setParameter("fEmail", email)
                .uniqueResult();
        session.getTransaction().commit();
        session.close();
        return Optional.ofNullable(user);
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        Session session = sf.openSession();
        session.getTransaction();
        User user = session.createQuery(
                "from ru.job4j.todo.model.User u where u.email = :fEmail and u.password = :fPass", User.class)
                .setParameter("fEmail", email)
                .setParameter("fPass", password)
                .uniqueResult();
        session.close();
        return Optional.ofNullable(user);
    }
}
