-- Seeds the first ADMIN user.
-- Email and password come from Flyway placeholders (spring.flyway.placeholders in application.yaml),
-- which read ADMIN_EMAIL / ADMIN_PASSWORD from the environment, so no credentials live in the repo.
-- The password is hashed with bcrypt ($2a$), compatible with Spring's BCryptPasswordEncoder.

create extension if not exists pgcrypto;

insert into users (id, email, first_name, last_name, role, hashed_password, created_at, updated_at)
values (
           nextval('user_seq'),
           lower('${adminEmail}'),
           'Admin',
           'Admin',
           'ADMIN',
           crypt('${adminPassword}', gen_salt('bf', 10)),
           now(),
           now()
       )
on conflict (email) do nothing;
