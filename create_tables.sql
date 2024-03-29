drop table if exists symmetric_aggs;
drop table if exists o_agents_profile;
drop table if exists strategies;
drop table if exists profiles;

create table profiles
(
	profile_id integer not null AUTO_INCREMENT,
	assignment varchar(500),
	num_strategies integer not null,
	PRIMARY KEY (profile_id)
);

create table strategies
(
	strategy_id integer NOT NULL AUTO_INCREMENT,
	profile_encoding integer not null,
	PRIMARY KEY (strategy_id)
);

create table o_agents_profile
(
	o_agents_profile_id integer NOT NULL AUTO_INCREMENT,
	profile_encoding integer not null,
	PRIMARY KEY (o_agents_profile_id,profile_encoding)
);

create table symmetric_aggs
(
	profile_id integer,
	strategy_id integer,
	o_agents_profile_id integer,
	num_players integer,
	payoff double,
	PRIMARY KEY (profile_id, strategy_id),
	FOREIGN KEY (profile_id)
	REFERENCES profiles (profile_id),
	FOREIGN KEY (strategy_id)
	REFERENCES strategies (strategy_id),
	FOREIGN KEY (o_agents_profile_id)
	REFERENCES o_agents_profile (o_agents_profile_id)
);

CREATE OR REPLACE VIEW best_response_payoffs as
SELECT o_agents_profile_id, MAX(payoff) AS payoff
FROM symmetric_aggs
GROUP BY o_agents_profile_id;

CREATE OR REPLACE VIEW nash as
SELECT sa.profile_id
FROM best_response_payoffs brp, symmetric_aggs sa, profiles p
WHERE brp.o_agents_profile_id = sa.o_agents_profile_id
AND   brp.payoff = sa.payoff
AND   p.profile_id = sa.profile_id
AND   sa.strategy_id IN (select distinct strategy_id from strategies)
GROUP BY p.profile_id, p.num_strategies
HAVING COUNT(*) = p.num_strategies;

/*
************test code ignore************

insert into profiles (profile_id, num_strategies) VALUES (1,1);
insert into profiles (profile_id, num_strategies) VALUES (2,2);
insert into profiles (profile_id, num_strategies) VALUES (3,1);

insert into strategies (strategy_id, strategy_name) VALUES (1,'Cooperate');
insert into strategies (strategy_id, strategy_name) VALUES (2,'Defect');

insert into o_agents_profile (o_agents_profile_id, o_agents_profile) VALUES (1,'Cooperate');
insert into o_agents_profile (o_agents_profile_id, o_agents_profile) VALUES (2,'Defect');

insert into symmetric_aggs (profile_id, strategy_id, o_agents_profile_id, num_players, payoff) VALUES (1,1,1,2,8);
insert into symmetric_aggs (profile_id, strategy_id, o_agents_profile_id, num_players, payoff) VALUES (2,2,1,1,10);
insert into symmetric_aggs (profile_id, strategy_id, o_agents_profile_id, num_players, payoff) VALUES (2,1,2,1,0);
insert into symmetric_aggs (profile_id, strategy_id, o_agents_profile_id, num_players, payoff) VALUES (3,2,2,2,5);

SELECT sa.profile_id
FROM symmetric_aggs sa, profiles p
WHERE sa.profile_id = p.profile_id
AND strategy_id in (1,2)
GROUP BY sa.profile_id, num_strategies
HAVING COUNT(*) = num_strategies;


//deviation set
SELECT DISTINCT profile_id
FROM 
(
SELECT o_agents_profile_id
FROM symmetric_aggs
WHERE profile_id = 3
) oa, symmetric_aggs sa
where oa.o_agents_profile_id = sa.o_agents_profile_id;

select *
from symmetric_aggs sa1, symmetric_aggs sa2
where sa1.

SELECT sa.profile_id
FROM best_response_payoffs brp, symmetric_aggs sa, profiles p
WHERE brp.o_agents_profile_id = sa.o_agents_profile_id
AND   brp.payoff = sa.payoff
AND   p.profile_id = sa.profile_id
AND   sa.strategy_id IN (select distinct strategy_id from strategies)
GROUP BY p.profile_id, p.num_strategies
HAVING COUNT(*) = p.num_strategies;

SELECT MAX(sa.payoff-oa.payoff)
FROM
(
SELECT o_agents_profile_id, payoff
FROM symmetric_aggs
WHERE profile_id = 3
) oa, symmetric_aggs sa
where oa.o_agents_profile_id = sa.o_agents_profile_id;

SELECT DISTINCT profile_id
FROM (
SELECT o_agents_profile_id
FROM symmetric_groups
WHERE profile_id = : prof_id 
) s
LEFT JOIN symmetric_aggs
USING ( o_agents_profile_id )

SELECT DISTINCT profile_id
FROM (
SELECT o_agents_profile_id
FROM subgame(:strats_in_support)
JOIN symmetric_aggs
USING(profile_id)
) oa, symmetric_aggs sa
where oa.o_agents_profile_id = sa.o_agents_profile_id;

SELECT sa.profile_id
FROM best_response_payoffs brp, symmetric_aggs sa, profiles p
WHERE brp.o_agents_profile_id = sa.o_agents_profile_id
AND   brp.payoff = sa.payoff
AND   p.profile_id = sa.profile_id
AND   sa.strategy_id IN (:allowed_strats)
GROUP BY p.profile_id, p.num_strategies
HAVING COUNT(*) = p.num_strategies;


//get nash
SELECT sa.profile_id
FROM best_response_payoffs brp, symmetric_aggs sa, profiles p
WHERE brp.o_agents_profile_id = sa.o_agents_profile_id
AND   brp.payoff = sa.payoff
AND   p.profile_id = sa.profile_id
AND   sa.strategy_id IN (select distinct strategy_id from strategies)
GROUP BY p.profile_id, p.num_strategies
HAVING COUNT(*) = p.num_strategies;


//count nash
select nash, total, nash/total, total/5242880 from
(
select count(*) as nash from (
SELECT sa.profile_id
FROM best_response_payoffs brp, symmetric_aggs sa, profiles p
WHERE brp.o_agents_profile_id = sa.o_agents_profile_id
AND   brp.payoff = sa.payoff
AND   p.profile_id = sa.profile_id
AND   sa.strategy_id IN (select distinct strategy_id from strategies)
GROUP BY p.profile_id, p.num_strategies
HAVING COUNT(*) = p.num_strategies) a
) b,
(
select count(*) as total from profiles
) c;

select profile_id, o_agents_profile_id, sum(payoff)
from symmetric_aggs
where profile_id in
(
	select profile_id from nash
)
group by profile_id, o_agents_profile_id
limit 10;

USING ( o_agents_profile_id , payoff )
INNER JOIN profiles USING ( profile_id )
WHERE strategy_id = ANY (: allowed_strats )
GROUP BY profile_id , num_strategies
HAVING COUNT (*) = num_strategies


select sa1.strategy_id
from symmetric_aggs sa1, symmetric_aggs sa2
where sa1.profile_id <> sa2.profile_id
and sa1.strategy_id = sa2.strategy_id
group by sa1.strategy_id
having count(*) > 2
limit 10;


select distinct sa1.profile_id, sa1.strategy_id , sa2.profile_id, sa2.strategy_id, sa3.profile_id, sa3.strategy_id, sa4.profile_id, sa4.strategy_id
from symmetric_aggs sa1, symmetric_aggs sa2, symmetric_aggs sa3, symmetric_aggs sa4
where sa1.profile_id <> sa2.profile_id
and sa1.strategy_id = sa2.strategy_id
and sa1.strategy_id = sa3.strategy_id
and sa3.profile_id <> sa4.profile_id
and sa3.strategy_id = sa4.strategy_id
limit 10;
*/