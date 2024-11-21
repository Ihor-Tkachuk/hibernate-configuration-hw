package mate.academy.service;

import mate.academy.DataProcessingException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Movie;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Service
public class MovieServiceImpl implements MovieService {
    private final SessionFactory sessionFactory;

    @Inject
    public MovieServiceImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Movie add(Movie movie) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(movie);
            transaction.commit();
            return movie;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Error adding Movie: " + movie, e);
        } finally {
            assert session != null;
            session.close();
        }
    }

    @Override
    public Movie get(Long id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            return session.get(Movie.class, id);
        } catch (Exception e) {
            throw new DataProcessingException("Error retrieving Movie with ID " + id, e);
        } finally {
            assert session != null;
            session.close();
        }
    }
}