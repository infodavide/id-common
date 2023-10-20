package org.infodavid.commons.persistence.impl.repository;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.PersistentObject;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.model.query.Restriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.Repository;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

/**
 * The Class RepositoryUtils.
 */
@SuppressWarnings("static-method")
@JsonIgnoreType
public class RepositoryUtils {

    /** The Constant FLUSH_METHOD. */
    private static final String FLUSH_METHOD = "flush";

    /** The singleton. */
    private static WeakReference<RepositoryUtils> instance = null;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryUtils.class);

    /** The Constant SAVE_METHOD. */
    private static final String SAVE_METHOD = "save";

    /**
     * returns the singleton.
     * @return the singleton
     */
    public static synchronized RepositoryUtils getInstance() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new RepositoryUtils());
        }

        return instance.get();
    }

    /**
     * Instantiates a new utilities.
     */
    private RepositoryUtils() {
        // noop
    }

    /**
     * Find.
     * @param <K>                 the key type
     * @param <T>                 the generic type
     * @param <R>                 the generic type
     * @param domainClass         the domain class
     * @param targetClass         the target class
     * @param entityManager       the entity manager
     * @param sqlResultSetMapping the SQL result set mapping
     * @param pagination          the pagination
     * @param restriction         the restriction
     * @return the page
     */
    @SuppressWarnings("unchecked")
    public <K extends Serializable, T extends PersistentObject<K>, R> Page<R> find(final Class<T> domainClass, final Class<R> targetClass, final EntityManager entityManager, final String sqlResultSetMapping, final Pagination pagination, final Restriction restriction) {
        final SqlResultSetMapping mapping = getSqlResultSetMapping(domainClass, sqlResultSetMapping);
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> criteriaCountQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> root = criteriaCountQuery.from(domainClass);
        Predicate predicate = toPredicate(criteriaBuilder, root, restriction);
        criteriaCountQuery.select(criteriaBuilder.count(root));

        if (predicate != null) {
            criteriaCountQuery.where(predicate);
        }

        final TypedQuery<Long> countQuery = entityManager.createQuery(criteriaCountQuery);
        LOGGER.debug("Counting...");
        final Long count = countQuery.getSingleResult();
        final Page<R> result = new Page<>(pagination);
        final List<R> list = new ArrayList<>();
        result.setResults(list);
        result.setTotalSize(count.longValue());

        LOGGER.debug("Page before querying: {}", result);

        if (count.longValue() == 0) {
            result.setSize(0);
        } else {
            final CriteriaQuery<R> criteriaQuery = criteriaBuilder.createQuery(targetClass);
            root = criteriaQuery.from(domainClass);

            if (mapping != null && mapping.classes() != null && mapping.classes().length > 0) {
                final ConstructorResult constructorResult = mapping.classes()[0];
                final Collection<Selection<?>> selection = new ArrayList<>();

                for (final ColumnResult columnResult : constructorResult.columns()) {
                    LOGGER.debug("Selecting column: {}", columnResult.name());
                    selection.add(root.get(columnResult.name()));
                }

                criteriaQuery.select(criteriaBuilder.construct(constructorResult.targetClass(), selection.toArray(new Selection<?>[selection.size()])));
            }

            predicate = toPredicate(criteriaBuilder, root, restriction);

            if (predicate != null) {
                criteriaQuery.where(predicate);
            }

            final TypedQuery<R> query = entityManager.createQuery(criteriaQuery);

            if (pagination != null && pagination.getSize() > 0 && pagination.getNumber() > 0) {
                query.setFirstResult((pagination.getNumber() - 1) * pagination.getSize());
                query.setMaxResults(pagination.getSize());
            }

            LOGGER.debug("Querying...");

            for (final R item : query.getResultList()) {
                list.add(item);
            }

            result.setSize(list.size());
        }

        LOGGER.debug("Page after querying: {}", result);

        return result;
    }

    /**
     * Gets the sql result set mapping.
     * @param <T>         the generic type
     * @param entityClass the entity class
     * @param name        the name
     * @return the sql result set mapping
     */
    public <T> SqlResultSetMapping getSqlResultSetMapping(final Class<T> entityClass, final String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        final SqlResultSetMapping[] annotations = entityClass.getAnnotationsByType(SqlResultSetMapping.class);

        if (annotations == null || annotations.length == 0) {
            return null;
        }

        for (final SqlResultSetMapping annotation : annotations) {
            if (name.equals(annotation.name())) {
                return annotation;
            }
        }

        return null;
    }

    /**
     * Save.
     * @param <K>        the key type
     * @param <T>        the generic type
     * @param <S>        the generic type
     * @param type       the type
     * @param repository the repository
     * @param entities   the entities
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public <K extends Serializable, T extends PersistentObject<K>, S extends T> List<S> save(final Class<? extends Repository<T, K>> type, final Repository<T, K> repository, final Iterable<S> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        final List<S> result = new ArrayList<>();

        try {
            if (Proxy.isProxyClass(repository.getClass())) {
                final InvocationHandler handler = Proxy.getInvocationHandler(repository);
                Method method = null;

                for (final S entity : entities) {
                    if (method == null) {
                        method = type.getMethod(SAVE_METHOD, entity.getClass());
                    }

                    result.add((S) handler.invoke(repository, method, new Object[] {
                            entity
                    }));
                }

                handler.invoke(repository, type.getMethod(FLUSH_METHOD), null);
            } else {
                Method method = null;

                for (final S entity : entities) {
                    if (method == null) {
                        method = type.getMethod(SAVE_METHOD, entity.getClass());
                    }

                    result.add((S) method.invoke(repository, entity));
                }

                MethodUtils.invokeExactMethod(repository, FLUSH_METHOD);
            }
        } catch (final Throwable e) {
            throw new PersistenceException(e);
        }

        return result;
    }

    /**
     * Save.
     * @param <K>        the key type
     * @param <T>        the generic type
     * @param <S>        the generic type
     * @param type       the type
     * @param repository the repository
     * @param entity     the entity
     * @return the s
     */
    @SuppressWarnings("unchecked")
    public <K extends Serializable, T extends PersistentObject<K>, S extends T> S save(final Class<? extends Repository<T, K>> type, final Repository<T, K> repository, final S entity) {
        if (entity == null) {
            return null;
        }

        final S result;

        try {
            if (Proxy.isProxyClass(repository.getClass())) {
                final InvocationHandler handler = Proxy.getInvocationHandler(repository);
                result = (S) handler.invoke(repository, type.getMethod(SAVE_METHOD, entity.getClass()), new Object[] {
                        entity
                });
                handler.invoke(repository, type.getMethod(FLUSH_METHOD), null);
            } else {
                result = (S) MethodUtils.invokeExactMethod(repository, SAVE_METHOD, entity);
                MethodUtils.invokeExactMethod(repository, FLUSH_METHOD);
            }
        } catch (final Throwable e) {
            throw new PersistenceException(e);
        }

        return result;
    }

    /**
     * To number.
     * @param value the value
     * @return the number
     */
    public Number toNumber(final Object value) {
        if (value == null) {
            return NumberUtils.BYTE_ZERO;
        }

        if (value instanceof final Number n) {
            return n;
        }

        return NumberUtils.createNumber(String.valueOf(value));
    }

    /**
     * To predicate.
     * @param <T>             the generic type
     * @param criteriaBuilder the criteria builder
     * @param root            the root
     * @param restriction     the restriction
     * @return the predicate
     */
    public <T> Predicate toPredicate(final CriteriaBuilder criteriaBuilder, final Root<T> root, final Restriction restriction) {
        if (restriction == null || restriction.getOperator() == null || StringUtils.isEmpty(restriction.getField())) {
            return null;
        }

        Predicate predicate;

        switch (restriction.getOperator()) {
        case EQ: {
            predicate = criteriaBuilder.equal(root.get(restriction.getField()), restriction.getValue());

            break;
        }
        case INSENSITIVE_EQ: {
            final Path<Object> e = root.get(restriction.getField());

            if (String.class.isAssignableFrom(e.getJavaType()) && restriction.getValue() instanceof final String v) {
                predicate = criteriaBuilder.equal(criteriaBuilder.upper(e.as(String.class)), StringUtils.upperCase(v));
            } else {
                predicate = criteriaBuilder.equal(e, restriction.getValue());
            }

            break;
        }
        case GE: {
            predicate = criteriaBuilder.ge(root.get(restriction.getField()), toNumber(restriction.getValue()));

            break;
        }
        case GT: {
            predicate = criteriaBuilder.gt(root.get(restriction.getField()), toNumber(restriction.getValue()));

            break;
        }
        case LE: {
            predicate = criteriaBuilder.le(root.get(restriction.getField()), toNumber(restriction.getValue()));

            break;
        }
        case LT: {
            predicate = criteriaBuilder.lt(root.get(restriction.getField()), toNumber(restriction.getValue()));

            break;
        }
        case NOT_EQ: {
            final Path<Object> e = root.get(restriction.getField());
            predicate = criteriaBuilder.or(criteriaBuilder.isNull(e), criteriaBuilder.equal(e, restriction.getValue()).not());

            break;
        }
        case INSENSITIVE_NOT_EQ: {
            final Path<Object> e = root.get(restriction.getField());

            if (String.class.isAssignableFrom(e.getJavaType()) && restriction.getValue() instanceof final String v) {
                predicate = criteriaBuilder.or(criteriaBuilder.isNull(e), criteriaBuilder.equal(criteriaBuilder.upper(e.as(String.class)), StringUtils.upperCase(v)).not());
            } else {
                predicate = criteriaBuilder.or(criteriaBuilder.isNull(e), criteriaBuilder.equal(e, restriction.getValue()).not());
            }

            break;
        }
        case NOT_LIKE: {
            final Path<String> e = root.get(restriction.getField());
            predicate = criteriaBuilder.or(criteriaBuilder.isNull(e), criteriaBuilder.like(e, toString(restriction.getValue())).not());

            break;
        }
        case INSENSITIVE_NOT_LIKE: {
            final Path<String> e = root.get(restriction.getField());
            predicate = criteriaBuilder.or(criteriaBuilder.isNull(e), criteriaBuilder.like(criteriaBuilder.upper(e), StringUtils.upperCase(toString(restriction.getValue()))).not());

            break;
        }
        case NOT_NULL: {
            predicate = criteriaBuilder.isNotNull(root.get(restriction.getField()));

            break;
        }
        case NULL: {
            predicate = criteriaBuilder.isNull(root.get(restriction.getField()));

            break;
        }
        case LIKE: {
            final Path<String> e = root.get(restriction.getField());
            predicate = criteriaBuilder.and(criteriaBuilder.isNotNull(e), criteriaBuilder.like(e, toString(restriction.getValue())));

            break;
        }
        case INSENSITIVE_LIKE: {
            final Path<String> e = root.get(restriction.getField());
            predicate = criteriaBuilder.and(criteriaBuilder.isNotNull(e), criteriaBuilder.like(criteriaBuilder.upper(e), StringUtils.upperCase(toString(restriction.getValue()))));

            break;
        }
        default:
            throw new IllegalArgumentException("Unexpected value: " + restriction.getOperator());
        }

        if (restriction.getAnd() == null && restriction.getOr() == null) {
            return predicate;
        }

        if (restriction.getAnd() != null) {
            predicate = criteriaBuilder.and(predicate, toPredicate(criteriaBuilder, root, restriction.getAnd()));
        }

        if (restriction.getOr() != null) {
            predicate = criteriaBuilder.or(predicate, toPredicate(criteriaBuilder, root, restriction.getOr()));
        }

        return predicate;
    }

    /**
     * To string.
     * @param value the value
     * @return the string
     */
    public String toString(final Object value) {
        if (value == null) {
            return StringUtils.EMPTY;
        }

        return String.valueOf(value);
    }
}
