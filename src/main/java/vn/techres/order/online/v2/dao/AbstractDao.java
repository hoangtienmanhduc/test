package vn.techres.order.online.v1.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public abstract class AbstractDao<PK extends Serializable, T> {

    private final Class<T> persistentClass;

    public AbstractDao() {
        this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];
    }

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    protected CriteriaBuilder getBuilder() {
        return this.getSession().getCriteriaBuilder();
    }

    public T persist(T entity) {
        this.getSession().persist(entity);
        return entity;
    }

    public void merge(T entity) {
        this.getSession().merge(entity);
    }

    public void remove(T entity) {
        this.getSession().remove(entity);
    }

}
