CREATE DATABASE hip CHARACTER SET utf8 COLLATE utf8_general_ci;

use hip;

create table stroop_answer (
  answer_id                 bigint not null auto_increment,
  answer                    varchar(20),
  used_time                 double,
  user_username             varchar(20),
  quiz_quiz_id              bigint,
  constraint pk_stroop_answer primary key (answer_id))
;

create table answer (
  id                        bigint not null auto_increment,
  first_word                varchar(20),
  second_word               varchar(20),
  third_word                varchar(20),
  used_time                 double,
  countdown_result          varchar(20),
  user_username             varchar(20),
  quiz_id                   bigint,
  constraint pk_answer primary key (id))
;

create table experiment_schedule (
  id                        bigint not null auto_increment,
  name                      varchar(30) not null,
  no_of_trial               integer(2) not null,
  start_date                timestamp not null,
  expire_date               timestamp not null,
  experiment_type           integer,
  constraint ck_experiment_schedule_experiment_type check (experiment_type in (0,1)),
  constraint pk_experiment_schedule primary key (id))
;

create table stroop_engword_question (
  question_id               bigint not null auto_increment,
  color_word                varchar(20) not null,
  ink_color                 varchar(20) not null,
  constraint pk_stroop_engword_question primary key (question_id))
;

create table question (
  id                        bigint not null auto_increment,
  first_word                varchar(20) not null,
  second_word               varchar(20) not null,
  third_word                varchar(20) not null,
  constraint pk_question primary key (id))
;

create table quiz (
  id                        bigint not null auto_increment,
  init_countdown            integer(3),
  flash_time                integer(20),
  trial_id                  bigint,
  question_id               bigint,
  constraint pk_quiz primary key (id))
;

create table stroop_quiz (
  quiz_id                   bigint not null auto_increment, 
  trial_trial_id            bigint,
  question_question_id      bigint,
  constraint pk_stroop_quiz primary key (quiz_id))
;

create table time_log (
  id                        bigint not null auto_increment,
  start_time                timestamp,
  end_time                  timestamp,
  user_username             varchar(20),
  trial_id                  bigint,
  constraint pk_time_log primary key (id))
;

create table stroop_time_log (
  time_log_id               bigint not null auto_increment,
  start_time                timestamp,
  end_time                  timestamp,
  trial_trial_id            bigint,
  user_username             varchar(20),
  constraint pk_stroop_time_log primary key (time_log_id))
;

create table trial (
  id                        bigint not null auto_increment,
  trigram_type              varchar(20) not null,
  trigram_language          varchar(20) not null,
  schedule_id               bigint,
  constraint pk_trial primary key (id))
;

create table stroop_trial (
  trial_id                  bigint not null auto_increment,
  appear_time               bigint(2) not null,
  question_type             integer(20) not null,
  color_match               boolean not null,
  schedule_id               bigint,
  constraint ck_stroop_trial_question_type check (question_type in (0,1,2)),
  constraint pk_stroop_trial primary key (trial_id))
;

create table user (
  username                  varchar(20) not null,
  password                  varchar(20) not null,
  status                    integer,
  constraint ck_user_status check (status in (0,1,2,3)),
  constraint pk_user primary key (username))
;

alter table stroop_answer add constraint fk_stroop_answer_user_1 foreign key (user_username) references user (username) on delete restrict on update restrict;
create index ix_stroop_answer_user_1 on stroop_answer (user_username);
alter table stroop_answer add constraint fk_stroop_answer_quiz_2 foreign key (quiz_quiz_id) references stroop_quiz (quiz_id) on delete restrict on update restrict;
create index ix_stroop_answer_quiz_2 on stroop_answer (quiz_quiz_id);
alter table answer add constraint fk_answer_user_3 foreign key (user_username) references user (username) on delete restrict on update restrict;
create index ix_answer_user_3 on answer (user_username);
alter table answer add constraint fk_answer_quiz_4 foreign key (quiz_id) references quiz (id) on delete restrict on update restrict;
create index ix_answer_quiz_4 on answer (quiz_id);
alter table quiz add constraint fk_quiz_trial_5 foreign key (trial_id) references trial (id) on delete restrict on update restrict;
create index ix_quiz_trial_5 on quiz (trial_id);
alter table quiz add constraint fk_quiz_question_6 foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_quiz_question_6 on quiz (question_id);
alter table stroop_quiz add constraint fk_stroop_quiz_trial_7 foreign key (trial_trial_id) references stroop_trial (trial_id) on delete restrict on update restrict;
create index ix_stroop_quiz_trial_7 on stroop_quiz (trial_trial_id);
alter table stroop_quiz add constraint fk_stroop_quiz_question_8 foreign key (question_question_id) references stroop_engword_question (question_id) on delete restrict on update restrict;
create index ix_stroop_quiz_question_8 on stroop_quiz (question_question_id);
alter table time_log add constraint fk_time_log_user_9 foreign key (user_username) references user (username) on delete restrict on update restrict;
create index ix_time_log_user_9 on time_log (user_username);
alter table time_log add constraint fk_time_log_trial_10 foreign key (trial_id) references trial (id) on delete restrict on update restrict;
create index ix_time_log_trial_10 on time_log (trial_id);
alter table stroop_time_log add constraint fk_stroop_time_log_trial_11 foreign key (trial_trial_id) references stroop_trial (trial_id) on delete restrict on update restrict;
create index ix_stroop_time_log_trial_11 on stroop_time_log (trial_trial_id);
alter table stroop_time_log add constraint fk_stroop_time_log_user_12 foreign key (user_username) references user (username) on delete restrict on update restrict;
create index ix_stroop_time_log_user_12 on stroop_time_log (user_username);
alter table trial add constraint fk_trial_schedule_13 foreign key (schedule_id) references experiment_schedule (id) on delete restrict on update restrict;
create index ix_trial_schedule_13 on trial (schedule_id);
alter table stroop_trial add constraint fk_stroop_trial_schedule_14 foreign key (schedule_id) references experiment_schedule (id) on delete restrict on update restrict;
create index ix_stroop_trial_schedule_14 on stroop_trial (schedule_id);