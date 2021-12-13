package com.oracle.interview;

import com.oracle.interview.auth.SimpleDatabaseAuthenticator;
import com.oracle.interview.auth.RoleBasedAuthorizer;
import com.oracle.interview.db.ActivityRepository;
import com.oracle.interview.db.ActivityRepositoryImpl;
import com.oracle.interview.db.UserRepository;
import com.oracle.interview.db.UserRepositoryImpl;
import com.oracle.interview.db.entity.Activity;
import com.oracle.interview.db.entity.User;
import com.oracle.interview.resources.ActivityController;
import com.oracle.interview.resources.LoginController;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class TMSApplication extends Application<TMSConfiguration> {

    public static void main(final String[] args) throws Exception {
        new TMSApplication().run(args);
    }

    @Override
    public String getName() {
        return "TMS";
    }

    @Override
    public void initialize(final Bootstrap<TMSConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        bootstrap.addBundle(new MigrationsBundle<TMSConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(TMSConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });

        bootstrap.addBundle(new SwaggerBundle<TMSConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(TMSConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });

        bootstrap.addBundle(hibernateBundle);

        // Other init code
        bootstrap.addBundle(new AssetsBundle("/apidocs", "/apidocs", "index.html")
        );
    }

    @Override
    public void run(final TMSConfiguration configuration, final Environment environment) {
        final UserRepository daoUser = new UserRepositoryImpl(hibernateBundle.getSessionFactory());
        final ActivityRepository dao = new ActivityRepositoryImpl(hibernateBundle.getSessionFactory());
        environment.jersey().register(new ActivityController(dao));
        environment.jersey().register(new LoginController());

        SimpleDatabaseAuthenticator simpleDatabaseAuthenticator = new
                UnitOfWorkAwareProxyFactory(hibernateBundle)
                .create(SimpleDatabaseAuthenticator.class, UserRepository.class, daoUser);

        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(simpleDatabaseAuthenticator)
                .setAuthorizer(new RoleBasedAuthorizer())
                .buildAuthFilter()));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }


    private final HibernateBundle<TMSConfiguration> hibernateBundle =
            new HibernateBundle<TMSConfiguration>(Activity.class, User.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(TMSConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };
}
