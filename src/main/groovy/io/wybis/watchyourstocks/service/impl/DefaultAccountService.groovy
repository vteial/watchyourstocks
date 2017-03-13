package io.wybis.watchyourstocks.service.impl

import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.model.Account
import io.wybis.watchyourstocks.model.Branch
import io.wybis.watchyourstocks.model.Product
import io.wybis.watchyourstocks.model.User
import io.wybis.watchyourstocks.model.constants.AccountStatus
import io.wybis.watchyourstocks.model.constants.ProductType
import io.wybis.watchyourstocks.repository.AccountRepository
import io.wybis.watchyourstocks.repository.UserRepository
import io.wybis.watchyourstocks.service.AccountService
import io.wybis.watchyourstocks.service.EmployeeService
import io.wybis.watchyourstocks.service.ProductService
import io.wybis.watchyourstocks.service.exceptions.ModelAlreadyExistException
import io.wybis.watchyourstocks.util.Helper
import org.springframework.stereotype.Service

import javax.annotation.Resource

@Service
@Slf4j
class DefaultAccountService extends AbstractService implements AccountService {

    @Resource
    AccountRepository accountRepository

    @Resource
    UserRepository userRepository;

    @Resource
    EmployeeService employeeService

    @Resource
    ProductService ProductService

    @Override
    public void add(SessionDto sessionUser, Account model)
            throws ModelAlreadyExistException {

        model.status = AccountStatus.ACTIVE
        model.id = autoNumberService.nextNumber(sessionUser, Account.ID_KEY)

        Date now = new Date()
        model.prePersist(sessionUser, now)

        model = this.accountRepository.save(model)
    }

    @Override
    public void onBranchCreate(SessionDto sessionUser, Branch branch) {
    }

    @Override
    public void onProductCreate(SessionDto sessionUser, Product product) {

        List<User> models = this.employeeService.findByBranchId(product.branchId)
        models.each { employee ->
            Account account = new Account()
            account.with {
                aliasName = product.code
                type = product.type
                productId = product.id
                userId = employee.id
                branchId = product.branchId
            }
            account.name = product.code + '-' + Helper.capitalize(employee.firstName)
            if (employee.lastName) {
                account.name = account.name + ' ' + Helper.capitalize(employee.lastName)
            }
            this.add(sessionUser, account)
        }
    }

    @Override
    public void onEmployeeCreate(SessionDto sessionUser, User employee) {

        List<Product> products = this.productService.findByTypeAndBranchId(ProductType.CASH_CAPITAL, employee.branchId)
        products.each { product ->
            Account account = new Account()
            account.with {
                aliasName = product.code
                type = product.type
                isMinus = true
                productId = product.id
                userId = employee.id
                branchId = employee.branchId
            }
            account.name = product.code + '-' + Helper.capitalize(employee.firstName)
            if (employee.lastName) {
                account.name = account.name + ' ' + Helper.capitalize(employee.lastName)
            }
            this.add(sessionUser, account)

            employee.cashAccount = account
            employee.cashAccountId = account.id

            Date now = new Date()
            employee.preUpdate(sessionUser, now)

            this.userRepository.save(employee)
        }

        products = this.productService.findByTypeAndBranchId(ProductType.PROFIT_EMPLOYEE, employee.branchId)
        products.each { product ->
            Account account = new Account()
            account.with {
                aliasName = product.code
                type = product.type
                productId = product.id
                userId = employee.id
                branchId = employee.branchId
            }
            account.name = product.code + '-' + Helper.capitalize(employee.firstName)
            if (employee.lastName) {
                account.name = account.name + ' ' + Helper.capitalize(employee.lastName)
            }
            this.add(sessionUser, account)

            employee.profitAccount = account
            employee.profitAccountId = account.id

            Date now = new Date()
            employee.preUpdate(sessionUser, now)

            this.userRepository.save(employee)
        }

        products = this.productService.findByTypeAndBranchId(ProductType.PRODUCT, employee.branchId)
        products.each { product ->
            Account account = new Account()
            account.with {
                aliasName = product.code
                type = product.type
                productId = product.id
                userId = employee.id
                branchId = employee.branchId
            }
            account.name = product.code + '-' + Helper.capitalize(employee.firstName)
            if (employee.lastName) {
                account.name = account.name + ' ' + Helper.capitalize(employee.lastName)
            }
            this.add(sessionUser, account)
        }
    }

    @Override
    public void onBrokerCreate(SessionDto sessionUser, User broker) {
        def entitys = datastore.execute {
            from Product.class.simpleName
            where branchId == broker.branchId
            and type == ProductType.CASH_BROKER
            limit 1
        }

        entitys.each { entity ->
            Product product = entity as Product
            Account account = new Account()
            account.with {
                aliasName = product.code
                isMinus = true
                type = product.type
                productId = product.id
                userId = broker.id
                branchId = broker.branchId
            }
            account.name = product.code + '-' + Helper.capitalize(broker.firstName)
            if (broker.lastName) {
                account.name = account.name + ' ' + Helper.capitalize(broker.lastName)
            }
            this.add(sessionUser, account)

            broker.cashAccount = account
            broker.cashAccountId = account.id
            broker.save()
        }
    }

    @Override
    public void onInvestorCreate(SessionDto sessionUser, User investor) {
        def entitys = datastore.execute {
            from Product.class.simpleName
            where branchId == investor.branchId
            and type == ProductType.CASH_INVESTOR
            limit 1
        }

        entitys.each { entity ->
            Product product = entity as Product
            Account account = new Account()
            account.with {
                aliasName = product.code
                isMinus = true
                type = product.type
                productId = product.id
                userId = investor.id
                branchId = investor.branchId
            }
            account.name = product.code + '-' + Helper.capitalize(investor.firstName)
            if (investor.lastName) {
                account.name = account.name + ' ' + Helper.capitalize(investor.lastName)
            }
            this.add(sessionUser, account)

            investor.cashAccount = account
            investor.cashAccountId = account.id
            investor.save()
        }
    }
}
