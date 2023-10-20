package org.infodavid.commons.persistence.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.infodavid.commons.model.EntityReference;
import org.infodavid.commons.model.NamedObject;
import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.PersistentObject;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.model.query.Restriction;
import org.infodavid.commons.persistence.dao.DefaultDao;

import com.github.javafaker.Faker;

import jakarta.persistence.PersistenceException;

/**
 * The Class AbstractDefaultDaoMock.
 * @param <K> the key type
 * @param <T> the generic type
 */
public abstract class AbstractDefaultDaoMock<K extends Serializable, T extends PersistentObject<K>> implements DefaultDao<K, T> {

    /** The map. */
    protected Map<K, T> map = new ConcurrentHashMap<>();

    /** The sequence. */
    protected final AtomicLong sequence = new AtomicLong(1);

    /**
     * Clear.
     */
    public void clear() {
        map.clear();
        sequence.set(1);
    }

    /**
     * Clone.
     * @param values the values
     * @return the list
     */
    protected List<T> clone(final Collection<T> values) {
        final List<T> results = new ArrayList<>();
        values.forEach(t -> results.add(clone(t)));

        return results;
    }

    /**
     * Clone.
     * @param value the value
     * @return the t
     */
    protected T clone(final T value) {
        if (value == null) {
            return null;
        }

        final Class<T> type = getDomainClass();

        try {
            return ConstructorUtils.invokeConstructor(type, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e); // NOSONAR Runtime exception
        }
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#count()
     */
    @Override
    public long count() throws PersistenceException {
        return map.size();
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#delete(org.infodavid.commons.model.PersistentObject)
     */
    @Override
    public void delete(final T entity) throws PersistenceException {
        if (entity == null || entity.getId() == null) {
            return;
        }

        map.remove(entity.getId());
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#deleteAll()
     */
    @Override
    public void deleteAll() throws PersistenceException {
        map.clear();
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#deleteAll(java.lang.Iterable)
     */
    @Override
    public void deleteAll(final Iterable<? extends T> entities) throws PersistenceException {
        if (entities == null) {
            return;
        }

        for (final T entity : entities) {
            delete(entity);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#deleteAllById(java.lang.Iterable)
     */
    @Override
    public void deleteAllById(final Iterable<? extends K> identifiers) throws PersistenceException {
        if (identifiers == null) {
            return;
        }

        for (final K id : identifiers) {
            deleteById(id);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#deleteById(java.io.Serializable)
     */
    @Override
    public void deleteById(final K id) throws PersistenceException {
        map.remove(id);
    }

    /**
     * Delete by name.
     * @param value the value
     * @throws PersistenceException the persistence exception
     */
    @SuppressWarnings("unchecked")
    public void deleteByName(final String value) throws PersistenceException {
        if (NamedObject.class.isAssignableFrom(getDomainClass())) {
            throw new NotImplementedException("Mock does not implement deleteByName method");
        }

        K id = null;

        for (final Entry<K, T> entry : map.entrySet()) {
            if (StringUtils.equals(((NamedObject<K>) entry.getValue()).getName(), value)) {
                id = entry.getKey();

                break;
            }
        }

        if (id != null) {
            map.remove(id);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#existsById(java.io.Serializable)
     */
    @Override
    public boolean existsById(final K id) throws PersistenceException {
        return map.containsKey(id);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#findAll()
     */
    @Override
    public Iterable<T> findAll() throws PersistenceException {
        final List<T> results = new ArrayList<>();
        map.values().forEach(t -> results.add(clone(t)));

        return results;
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#findAll(org.infodavid.commons.model.query.Pagination, org.infodavid.commons.model.query.Restriction)
     */
    @Override
    public Page<T> findAll(final Pagination pagination, final Restriction restriction) throws PersistenceException {
        // TODO Mocking of search by restriction
        final Page<T> result = new Page<>(pagination);
        final List<T> entities = new ArrayList<>();
        map.values().forEach(t -> entities.add(clone(t)));
        Collections.sort(entities, getComparator());
        result.setTotalSize(entities.size());
        int number = 0;
        int size = 0;

        if (pagination != null) {
            number = pagination.getNumber();
            size = pagination.getSize();
        }

        if (number < 0 || size <= 0) {
            result.setResults(entities);
        } else {
            final int offset = (number - 1) * size;
            result.setResults(entities.subList(offset, offset + size));
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#findAllById(java.lang.Iterable)
     */
    @Override
    public Iterable<T> findAllById(final Iterable<K> identifiers) throws PersistenceException {
        if (identifiers == null) {
            return Collections.emptyList();
        }

        final List<T> results = new ArrayList<>();

        for (final K id : identifiers) {
            final Optional<T> optional = findById(id);

            if (optional.isPresent()) {
                results.add(clone(optional.get()));
            }
        }

        return results;
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#findById(java.io.Serializable)
     */
    @Override
    public Optional<T> findById(final K id) throws PersistenceException {
        final T result = map.get(id);

        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(clone(result));
    }

    /**
     * Find by name.
     * @param value the value
     * @return the entity or null if not found
     * @throws PersistenceException the persistence exception
     */
    @SuppressWarnings("unchecked")
    public T findByName(final String value) throws PersistenceException {
        if (NamedObject.class.isAssignableFrom(getDomainClass())) {
            throw new NotImplementedException("Mock does not implement findByName method");
        }

        for (final Entry<K, T> entry : map.entrySet()) {
            if (StringUtils.equals(((NamedObject<K>) entry.getValue()).getName(), value)) {
                return clone(entry.getValue());
            }
        }

        return null;
    }

    /**
     * Find references.
     * @param pagination  the pagination
     * @param restriction the restriction
     * @return the page
     * @throws PersistenceException the persistence exception
     */
    @SuppressWarnings("unchecked")
    public Page<EntityReference> findReferences(final Pagination pagination, final Restriction restriction) throws PersistenceException {
        if (NamedObject.class.isAssignableFrom(getDomainClass())) {
            throw new NotImplementedException("Mock does not implement findReferences method");
        }

        // TODO Mocking of search by restriction
        final Page<EntityReference> result = new Page<>(pagination);
        final List<T> entities = new ArrayList<>(map.values());
        Collections.sort(entities, getComparator());
        result.setTotalSize(entities.size());
        result.setResults(new ArrayList<>());
        int number = 0;
        int size = 0;

        if (pagination != null) {
            number = pagination.getNumber();
            size = pagination.getSize();
        }

        if (number < 0 || size <= 0) {
            entities.forEach(e -> result.getResults().add(new EntityReference(e.getId(), ((NamedObject<K>) e).getName())));
        } else {
            final int offset = (number - 1) * size;
            entities.subList(offset, offset + size).forEach(e -> result.getResults().add(new EntityReference(e.getId(), ((NamedObject<K>) e).getName())));
        }

        return result;
    }

    /**
     * Gets the comparator.
     * @return the comparator
     */
    @SuppressWarnings("unchecked")
    protected Comparator<T> getComparator() {
        return (l, r) -> {
            if (l == null && r == null) {
                return 0;
            }

            if (l == null || l.getId() == null) {
                return 1;
            }

            if (r == null || r.getId() == null) {
                return -1;
            }

            return ((Comparable<T>) l.getId()).compareTo((T) r.getId());
        };
    }

    /**
     * Gets the domain class.
     * @return the class
     */
    protected abstract Class<T> getDomainClass();

    /**
     * Gets the identifier class.
     * @return the class
     */
    protected abstract Class<K> getIdClass();

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#insert(org.infodavid.commons.model.PersistentObject)
     */
    @Override
    public <S extends T> S insert(final S entity) throws PersistenceException {
        if (entity.getId() == null) {
            entity.setId(nextId());
        }

        entity.setCreationDate(new Date());
        entity.setModificationDate(entity.getCreationDate());
        map.put(entity.getId(), clone(entity));

        return entity;
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#insertAll(java.lang.Iterable)
     */
    @Override
    public <S extends T> List<S> insertAll(final Iterable<S> entities) throws PersistenceException {
        if (entities == null) {
            return Collections.emptyList();
        }

        final List<S> results = new ArrayList<>();

        for (final S entity : entities) {
            results.add(insert(entity));
        }

        return results;
    }

    /**
     * Next id.
     * @return the new identifier
     */
    @SuppressWarnings("unchecked")
    protected K nextId() {
        return (K) Long.valueOf(sequence.getAndIncrement());
    }

    /**
     * Populate.
     * @param size the size
     * @return the list
     * @throws InstantiationException the instantiation exception
     */
    public List<T> populate(final int size) throws InstantiationException {
        final List<T> results = new ArrayList<>();
        final Faker faker = new Faker();

        try {
            for (int i = 0; i < size; i++) {
                results.add(insert(populateEntity(faker, getDomainClass().getConstructor().newInstance())));
            }
        } catch ( final InstantiationException e) {
            throw e;

        } catch (PersistenceException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new InstantiationException(e.getMessage());

        }

        return results;
    }

    /**
     * Populate entity.
     * @param faker  the faker
     * @param entity the entity
     * @return the entity
     */
    protected T populateEntity(final Faker faker, final T entity) {
        if (entity == null) {
            return null;
        }

        if (entity instanceof final NamedObject<?> no) {
            no.setName(faker.name().username());
        }

        return entity;
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#update(org.infodavid.commons.model.PersistentObject)
     */
    @Override
    public <S extends T> S update(final S entity) throws PersistenceException {
        entity.setModificationDate(new Date());
        map.put(entity.getId(), clone(entity));

        return entity;
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#updateAll(java.lang.Iterable)
     */
    @Override
    public <S extends T> List<S> updateAll(final Iterable<S> entities) throws PersistenceException {
        if (entities == null) {
            return Collections.emptyList();
        }

        final List<S> results = new ArrayList<>();

        for (final S entity : entities) {
            results.add(update(entity));
        }

        return results;
    }
}
