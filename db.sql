create database "SWE"
    with owner "SWE_owner";

grant connect, create, temporary on database "SWE" to neon_superuser;

create table public.appuser
(
    id            serial
        primary key,
    name          varchar(100) not null,
    surname       varchar(100) not null,
    email         varchar(150) not null
        unique,
    password_hash text         not null
);

alter table public.appuser
    owner to "SWE_owner";

create table public.event
(
    id                serial
        primary key,
    title             varchar(200)   not null,
    description       text,
    date              timestamp      not null,
    tickets_available integer        not null
        constraint event_tickets_available_check
            check (tickets_available >= 0),
    ticket_price      numeric(10, 2) not null
        constraint event_ticket_price_check
            check (ticket_price >= (0)::numeric)
    );

alter table public.event
    owner to "SWE_owner";

create table public.ticket
(
    id       serial
        primary key,
    user_id  integer not null
        constraint fk_ticket_user_id
            references public.appuser
            on delete cascade,
    quantity integer not null
        constraint ticket_quantity_check
            check (quantity > 0),
    event_id integer not null
        constraint fk_ticket_event_id
            references public.event
            on delete cascade,
    used     boolean not null
);

alter table public.ticket
    owner to "SWE_owner";

create table public.staff
(
    user_id  integer not null
        constraint fk_staff_user
            references public.appuser
            on delete cascade,
    event_id integer not null
        constraint fk_staff_event
            references public.event
            on delete cascade,
    primary key (user_id, event_id)
);

alter table public.staff
    owner to "SWE_owner";

create table public.admin
(
    user_id  integer not null
        constraint fk_admin_user
            references public.appuser
            on delete cascade,
    event_id integer not null
        constraint fk_admin_event
            references public.event
            on delete cascade,
    primary key (user_id, event_id)
);

alter table public.admin
    owner to "SWE_owner";

