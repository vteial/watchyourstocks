package io.wybis.watchyourstocks.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.model.Branch
import io.wybis.watchyourstocks.repository.BranchRepository
import io.wybis.watchyourstocks.service.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import javax.annotation.Resource

@Service
@Slf4j
public class DefaultAppService extends AbstractService implements
        AppService {

    static void main(def args) {
        println 'started...'

        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setHost('smtp.sendgrid.net');
        mailSenderImpl.setPort(587);
        mailSenderImpl.setUsername('hBsrFC8YkB1i6741');
        mailSenderImpl.setPassword('WgMNhjmCqC');

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setFrom('no-reply@watchyourstocks.wybis.com');
        emailMessage.setTo('vteial@gmail.com');
        emailMessage.setSubject('WatchYourStocks - Test Mail');
        emailMessage.setText('This is test mail. Please, ignore it.');

        mailSenderImpl.send(emailMessage)

        println 'finished...'
    }

    @Resource
    BranchRepository branchRepository

    @Resource
    ClassPathResource jsonDefaultBranchCpr

    @Resource
    ObjectMapper jsonObjectMapper

    @Resource
    BranchService branchService

    @Resource
    ProductService productService

    @Resource
    EmployeeService employeeService

    @Resource
    BrokerService brokerService

    @Resource
    InvestorService investorService

    @PostConstruct
    @Override
    public void init() {
        log.debug('----------------------------------------------------------------------------')
        log.debug('app initialization started...')

        long count = branchRepository.count()
        if(count == 0) {
            Branch model = jsonObjectMapper.readValue(jsonDefaultBranchCpr.inputStream, Branch.class)

            SessionDto sessionUser = new SessionDto()
            branchService.add(sessionUser, model)

            model.products.each { t ->
                t.branchId = model.id
                productService.add(sessionUser, t)
            }

            model.employees.each { t ->
                t.branchId = model.id
                t.userId = "${t.userId}@${model.code}"
                employeeService.add(sessionUser, t)
            }

            model.brokers.each { t ->
                t.branchId = model.id
                brokerService.add(sessionUser, t)
            }

            model.investors.each { t ->
                t.branchId = model.id
                investorService.add(sessionUser, t)
            }

            log.info('app initialized...')
        } else {
            log.info('app already initialized...')
        }

        log.debug('app initialization finished...')
        log.debug('----------------------------------------------------------------------------')
    }

}
