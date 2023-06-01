--
-- PostgreSQL database dump
--

-- Dumped from database version 10.22
-- Dumped by pg_dump version 10.22

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: buses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.buses (
    bus_id character varying(10) NOT NULL,
    name character varying(20),
    type character varying(10),
    ac boolean,
    fare integer,
    available_seats integer,
    booked integer,
    cancelled integer
);


ALTER TABLE public.buses OWNER TO postgres;

--
-- Name: seater_seatings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.seater_seatings (
    bus_id character varying(10),
    row_id integer,
    a character varying(10),
    b character varying(10),
    c character varying(10)
);


ALTER TABLE public.seater_seatings OWNER TO postgres;

--
-- Name: sleeper_seatings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sleeper_seatings (
    bus_id character varying(10),
    row_id integer,
    a character varying(10),
    b character varying(10)
);


ALTER TABLE public.sleeper_seatings OWNER TO postgres;

--
-- Name: tickets; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tickets (
    ticket_id character varying(30) NOT NULL,
    email character varying(20),
    bus_id character varying(20),
    seat_number character varying(5),
    gender character varying(4),
    name character varying(20)
);


ALTER TABLE public.tickets OWNER TO postgres;

--
-- Name: transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transactions (
    id integer NOT NULL,
    ticket_id character varying(20),
    email character varying(20),
    status character varying(20),
    date timestamp without time zone DEFAULT LOCALTIMESTAMP(0)
);


ALTER TABLE public.transactions OWNER TO postgres;

--
-- Name: transactions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.transactions_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.transactions_id_seq OWNER TO postgres;

--
-- Name: transactions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.transactions_id_seq OWNED BY public.transactions.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    name character varying(40),
    email character varying(40) NOT NULL,
    password character varying(200),
    gender character(4),
    contact character varying(10),
    tickets_booked integer
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: transactions id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions ALTER COLUMN id SET DEFAULT nextval('public.transactions_id_seq'::regclass);


--
-- Data for Name: buses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.buses (bus_id, name, type, ac, fare, available_seats, booked, cancelled) FROM stdin;
non-ac001	Non-AC Seater	seater	f	450	10	2	0
ac002	AC Sleeper	sleeper	t	700	9	3	1
ac001	AC Seater	seater	t	550	9	3	0
non-ac002	Non-AC Sleeper	sleeper	f	600	11	1	0
\.


--
-- Data for Name: seater_seatings; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.seater_seatings (bus_id, row_id, a, b, c) FROM stdin;
ac001	3	A	A	A
ac001	4	A	A	A
non-ac001	3	A	A	A
non-ac001	4	A	A	A
non-ac001	2	A	A	M
non-ac001	1	A	F	A
ac001	1	M	A	A
ac001	2	M	F	A
\.


--
-- Data for Name: sleeper_seatings; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sleeper_seatings (bus_id, row_id, a, b) FROM stdin;
ac002	2	A	A
ac002	5	A	A
ac002	6	A	A
non-ac002	2	A	A
non-ac002	3	A	A
non-ac002	4	A	A
non-ac002	5	A	A
non-ac002	6	A	A
ac002	3	A	A
ac002	4	F	M
ac002	1	F	A
non-ac002	1	M	A
\.


--
-- Data for Name: tickets; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tickets (ticket_id, email, bus_id, seat_number, gender, name) FROM stdin;
ac002-A1	ajith@gmail.com	ac002	A1	F	Nandika
ac002-B4	deepak@gmail.com	ac002	B4	M	Deepak
ac002-A4	deepak@gmail.com	ac002	A4	F	JEEVIKA
non-ac001-C2	mainthan@gmail.com	non-ac001	C2	M	Durai
non-ac001-B1	mainthan@gmail.com	non-ac001	B1	F	MALAR
ac001-A1	ajith@gmail.com	ac001	A1	M	Naveen
ac001-B2	ajith@gmail.com	ac001	B2	F	Samyuktha
ac001-A2	deepak@gmail.com	ac001	A2	M	Peter
non-ac002-A1	ajith@gmail.com	non-ac002	A1	M	Karthi
\.


--
-- Data for Name: transactions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.transactions (id, ticket_id, email, status, date) FROM stdin;
2	ac002-A1	a	booked	2022-09-16 11:47:20
3	ac002-B4	a	booked	2022-09-16 11:51:14
4	ac002-A4	a	booked	2022-09-16 11:51:14
5	ac002-A1	a	cancelled	2022-09-16 12:10:32
6	ac002-A1	mainthan@gmail.com	booked	2022-09-16 12:53:01
7	ac002-A3	a	booked	2022-09-16 12:58:35
8	ac002-A4	a	cancelled	2022-09-16 13:00:21
9	ac002-A3	a	cancelled	2022-09-16 13:00:21
10	ac001-A1	a	booked	2022-09-16 13:17:49
11	ac001-A2	mainthan@gmail.com	booked	2022-09-16 13:19:03
12	ac001-a2	mainthan@gmail.com	cancelled	2022-09-16 13:38:32
13	ac002-a1	mainthan@gmail.com	cancelled	2022-09-16 13:44:02
14	ac002-B1	ajith@gmail.com	booked	2022-09-16 14:38:25
15	ac002-A1	ajith@gmail.com	booked	2022-09-16 14:38:25
16	ac002-B4	deepak@gmail.com	booked	2022-09-16 14:40:18
17	ac002-A4	deepak@gmail.com	booked	2022-09-16 14:40:18
18	non-ac001-C2	mainthan@gmail.com	booked	2022-09-16 14:42:11
19	non-ac001-B1	mainthan@gmail.com	booked	2022-09-16 14:42:11
20	ac002-b1	ajith@gmail.com	cancelled	2022-09-16 14:44:07
21	ac001-A1	ajith@gmail.com	booked	2023-04-25 10:37:31
22	ac001-B2	ajith@gmail.com	booked	2023-04-25 10:37:31
23	ac001-A2	deepak@gmail.com	booked	2023-04-25 10:42:51
24	non-ac002-A1	ajith@gmail.com	booked	2023-04-25 10:51:50
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (name, email, password, gender, contact, tickets_booked) FROM stdin;
Ramya	ramya@gmail.com	$2a$10$zwq2DNUuOJxV.kSiKFFiQ.6IsugZXsWCIySLNbaEhXPNPd4AhwHje	F   	9876543210	0
admin	admin@gmail.com	$2a$10$zwq2DNUuOJxV.kSiKFFiQ.6IsugZXsWCIySLNbaEhXPNPd4AhwHje	\N	\N	\N
Ajith Kumar P	a	$2a$10$zwq2DNUuOJxV.kSiKFFiQ.6IsugZXsWCIySLNbaEhXPNPd4AhwHje	M   	9876543210	0
Ajith	ajith@gmail.com	$2a$10$zwq2DNUuOJxV.kSiKFFiQ.6IsugZXsWCIySLNbaEhXPNPd4AhwHje	M   	6382778409	4
Deepak	deepak.r.m2002@gmail.com	$2a$10$zwq2DNUuOJxV.kSiKFFiQ.6IsugZXsWCIySLNbaEhXPNPd4AhwHje	M   	9443820773	0
Deepak kumar	deepak@gmail.com	$2a$10$zwq2DNUuOJxV.kSiKFFiQ.6IsugZXsWCIySLNbaEhXPNPd4AhwHje	M   	6382778409	3
KM	mainthan@gmail.com	$2a$10$zwq2DNUuOJxV.kSiKFFiQ.6IsugZXsWCIySLNbaEhXPNPd4AhwHje	M   	9876543210	2
Harini	harini@gmail.com	$2a$10$GZrNaPqhVqEURy7OYOUab.ZDEKCo/cbQPgFStno/7.YbFuan30Jji	F   	6382778409	0
\.


--
-- Name: transactions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.transactions_id_seq', 24, true);


--
-- Name: buses buses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.buses
    ADD CONSTRAINT buses_pkey PRIMARY KEY (bus_id);


--
-- Name: tickets tickets_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_pkey PRIMARY KEY (ticket_id);


--
-- Name: transactions transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (email);


--
-- Name: seater_seatings fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seater_seatings
    ADD CONSTRAINT fk FOREIGN KEY (bus_id) REFERENCES public.buses(bus_id);


--
-- Name: sleeper_seatings fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sleeper_seatings
    ADD CONSTRAINT fk FOREIGN KEY (bus_id) REFERENCES public.buses(bus_id);


--
-- Name: tickets fk1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT fk1 FOREIGN KEY (email) REFERENCES public.users(email);


--
-- Name: tickets fk2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT fk2 FOREIGN KEY (bus_id) REFERENCES public.buses(bus_id);


--
-- PostgreSQL database dump complete
--

