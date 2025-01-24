
/***********************************************************
 * 공통 유틸/함수 (기존과 동일)
 ***********************************************************/

function isLoggedIn() {
    return !!localStorage.getItem("loggedInUserId");
}

function getLoggedInUserId() {
    return localStorage.getItem("loggedInUserId");
}

function logout() {
    localStorage.removeItem("loggedInUserId");
    window.location.href = "/diggydog/login.html";
}

function showMessage(selector, msg, isError) {
    const el = document.querySelector(selector);
    if (el) {
        el.textContent = msg;
        el.style.color = isError ? 'red' : 'green';
    }
}

/***********************************************************
 * [1] 로그인 페이지 로직 (기존 login.html 용)
 ***********************************************************/

function initLogin() {
    // 이미 로그인 중이면 index.html로
    if (isLoggedIn()) {
        window.location.href = "/diggydog/index.html";
        return;
    }

    // loginBtn 클릭 시 login() 호출
    const loginBtn = document.getElementById('loginBtn');
    if (loginBtn) {
        loginBtn.addEventListener('click', login);
    }

    // 회원가입 버튼 이벤트 추가
    const signupBtn = document.getElementById('signupBtn');
    if (signupBtn) {
        signupBtn.addEventListener('click', () => {
            window.location.href = 'signup.html';
        });
    }

}

async function login() {
    const user_id = document.getElementById('user_id').value.trim();
    const password = document.getElementById('password').value;

    if (!user_id || !password) {
        showMessage('#message', "Please fill in both userId and password.", true);
        return;
    }

    try {
        // 서버에 로그인 요청
        const payload = { user_id, password };
        const url = `http://walab.handong.edu:8080/diggydog_3/api/user/login?user_id=${encodeURIComponent(user_id)}&password=${encodeURIComponent(password)}`;
        const res = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }  // GET 요청에서는 필요 없음, 제거 가능
        });


        // const res = await fetch('http://walab.handong.edu:8080/diggydog_3/api/user/login', {
        //     method: 'POST',
        //     headers: { 'Content-Type': 'application/json' },
        //     body: JSON.stringify(payload),
        // });

        // const res = await fetch(`http://walab.handong.edu:8080/diggydog_3/api/user/login?userId=${userId}&password=${password}`, {
        //     method: 'GET',
        //     headers: { 'Content-Type': 'application/json' }
        // });

        const result = await res.json();
        // 예:
        // {
        //   "status": 200,
        //   "message": "Login successful",
        //   "data": { "userId": "testuser", ... }
        // }

        if (res.ok) {
            // 로그인 성공
            const userData = result.data;
            // 로컬스토리지에 userId 저장
            localStorage.setItem("loggedInUserId", userData.userId);

            // index.html 로 이동
            window.location.href = "/diggydog/index.html";
        } else {
            // res.ok가 false인 경우, 상태코드별 처리
            // 404 -> User not found
            // 401 -> Invalid password
            // etc
            showMessage('#message', result.message || "Login failed", true);
        }
    } catch (error) {
        showMessage('#message', `Server Error: ${error.message}`, true);
    }
}


/***********************************************************
 * [] 회원가입 페이지 로직 (기존 signup.html 용)
 ***********************************************************/

// 회원가입 페이지 초기화
function initSignUp() {
    const checkAvailabilityBtn = document.getElementById('checkAvailabilityBtn');
    if (checkAvailabilityBtn) {
        checkAvailabilityBtn.addEventListener('click', checkUserIdAvailability);
    }

    const registerBtn = document.getElementById('registerBtn');
    if (registerBtn) {
        registerBtn.addEventListener('click', registerUser);
    }
}

// 유저 ID 중복 확인
async function checkUserIdAvailability() {
    const userId = document.getElementById('newUserId').value.trim();
    if (!userId) {
        showMessage('#availabilityMessage', 'Please enter a user ID.', true);
        return;
    }

    try {
        const res = await fetch(`http://walab.handong.edu:8080/diggydog_3/api/user/check-availability?userId=${encodeURIComponent(userId)}`, {
            method: 'GET',
        });

        const result = await res.json();

        if (res.ok && result.success) {
            showMessage('#availabilityMessage', 'User ID is available.', false);
        } else {
            showMessage('#availabilityMessage', 'User ID is already taken.', true);
        }
    } catch (error) {
        showMessage('#availabilityMessage', 'Server error occurred.', true);
    }
}

// 회원가입 처리
async function registerUser() {
    const newUserName = document.getElementById('newUserName').value.trim();
    const newUserId = document.getElementById('newUserId').value.trim();
    const newPassword = document.getElementById('newPassword').value.trim();
    const confirmPassword = document.getElementById('confirmPassword').value.trim();

    if (!newUserName||!newUserId || !newPassword || !confirmPassword) {
        showMessage('#signupMessage', 'Please fill in all fields.', true);
        return;
    }

    if (newPassword !== confirmPassword) {
        showMessage('#signupMessage', 'Passwords do not match.', true);
        return;
    }

    try {
        const payload = {userName: newUserName, userId: newUserId, password: newPassword };
        const res = await fetch('http://walab.handong.edu:8080/diggydog_3/api/user', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload),
        });

        const result = await res.json();

        if (res.ok && result.success) {
            showMessage('#signupMessage', 'Registration successful! Please login.', false);
            setTimeout(() => {
                window.location.href = "/diggydog/login.html";
            }, 2000);
        } else {
            showMessage('#signupMessage', result.message || 'Registration failed.', true);
        }
    } catch (error) {
        showMessage('#signupMessage', `Server Error: ${error.message}`, true);
    }
}

document.addEventListener('DOMContentLoaded', function () {
    // 인덱스 페이지 버튼 이벤트 리스너
    const createBookingBtn = document.getElementById('createBookingBtn');
    if (createBookingBtn) {
        createBookingBtn.addEventListener('click', () => {
            window.location.href = "createBooking.html";
        });
    }
});

document.addEventListener('DOMContentLoaded', function () {
    // 인덱스 페이지 버튼 이벤트 리스너
    const createBookingBtn = document.getElementById('createBookingBtn');
    if (createBookingBtn) {
        createBookingBtn.addEventListener('click', () => {
            window.location.href = "createBooking.html";
        });
    }
});


/***********************************************************
 * [] 부킹 가입 페이지 로직 ( 기존 createBooking.html 용 )
 ***********************************************************/




// Create Booking 페이지 초기화
function initCreateBooking() {
    const createBtn = document.getElementById('createBookingBtn');
    if (createBtn) {
        createBtn.addEventListener('click', createBooking);
    }

    // 시/분 select 옵션 채우기
    populateHourMinuteSelects();
}


// 예약 생성 처리
async function createBooking() {
    const dateValue = document.getElementById('date').value.trim();
    const hourValue = document.getElementById('hourSelect').value;
    const minuteValue = document.getElementById('minuteSelect').value;
    const bookingInfo = document.getElementById('bookingInfo').value.trim();
    const name = document.getElementById('name').value.trim();
    const noPeople = document.getElementById('noPeople').value.trim();
    const userId = localStorage.getItem("loggedInUserId");

    if (!dateValue || !bookingInfo || !name || !noPeople) {
        showMessage('#message', 'Please fill in all fields.', true);
        return;
    }

    // 시간 형식 설정 (HH:mm)
    //const bookingTime = `${hourValue.padStart(2, '0')}:${minuteValue.padStart(2, '0')}`;
    const formattedHour = String(hourValue).padStart(2, '0');
    const formattedMinute = String(minuteValue).padStart(2, '0');
    const bookingTime = `${formattedHour}:${formattedMinute}`;

    const payload = {
        userId,
        date: dateValue,
        bookingTime,
        bookingInfo,
        name,
        noPeople: parseInt(noPeople, 10)
    };

    try {
        const res = await fetch('http://walab.handong.edu:8080/diggydog_3/api/booking', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload),
        });

        const result = await res.json();

        if (res.ok && result.success) {
            showMessage('#message', 'Booking created successfully!', false);
            setTimeout(() => {
                window.location.href = "index.html";
            }, 2000);
        } else {
            showMessage('#message', result.message || 'Error creating booking.', true);
        }
    } catch (error) {
        showMessage('#message', `Server error: ${error.message}`, true);
    }
}



/***********************************************************
 * [2] 인덱스 페이지 로직 (기존 index.html 용)
 ***********************************************************/

function initIndex() {
    // 1. 로그인 여부 체크
    if (!isLoggedIn()) {
        window.location.href = "/diggydog/login.html";
        return;
    }

    // 2. 인사말 표시
    const userId = getLoggedInUserId();
    const contentDiv = document.getElementById('content');
    if (contentDiv) {
        contentDiv.textContent = `Welcome! You are logged in as userId: ${userId}`;
    }

    // 3. 로그아웃 버튼
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', logout);
    }

    // 4. Manage Booking 버튼 -> manageBooking.html 이동
    const manageBtn = document.getElementById('manageBtn');
    if (manageBtn) {
        manageBtn.addEventListener('click', () => {
            // 페이지 이동
            window.location.href = "/diggydog/manageBooking.html";
        });
    }

    // 5. 해당 유저의 예약 목록 가져오기
    fetchUserBookings(userId);
}

async function fetchUserBookings(userId) {
    try {
        const res = await fetch(`http://walab.handong.edu:8080/diggydog_3/api/booking/get?user_id=${encodeURIComponent(userId)}`);
        if (!res.ok) {
            throw new Error(`Failed to fetch bookings. Status: ${res.status}`);
        }
        const bookings = await res.json(); // List<BookingResponseDTO> 라고 가정
        displayBookings(bookings);
    } catch (err) {
        alert("Error loading bookings: " + err.message);
    }
}


function displayBookings(bookings) {
    const bookingListDiv = document.getElementById('bookingList');
    if (!bookingListDiv) return;

    bookingListDiv.innerHTML = ""; // 초기화

    if (!Array.isArray(bookings) || bookings.length === 0) {
        bookingListDiv.textContent = "No bookings found.";
        return;
    }

    bookings.forEach((b) => {
        const div = document.createElement('div');
        div.className = 'booking-box';
        div.innerHTML = `
      <p><strong>Booking ID:</strong> ${b.bookingId}</p>
      <p><strong>User ID:</strong> ${b.userId}</p>
      <p><strong>Name:</strong> ${b.name}</p>
      <p><strong>Date:</strong> ${b.date}</p>
      <p><strong>Time:</strong> ${b.bookingTime}</p>
      <p><strong>Info:</strong> ${b.bookingInfo}</p>
      <p><strong>No. People:</strong> ${b.noPeople}</p>
      <!-- Update 버튼 (있다면) -->
      <button class="updateBtn" data-booking-id="${b.bookingId}">Update</button>
      <!-- Delete 버튼 -->
      <button class="deleteBtn" data-booking-id="${b.bookingId}">Delete</button>
    `;
        bookingListDiv.appendChild(div);
    });

    // (이미 Update 버튼 이벤트가 있다면, 아래처럼 처리)
    const updateButtons = bookingListDiv.querySelectorAll('.updateBtn');
    updateButtons.forEach((btn) => {
        btn.addEventListener('click', (e) => {
            const bookingId = e.target.getAttribute('data-booking-id');
            window.location.href = `/diggydog/manageBooking.html?bookingId=${encodeURIComponent(bookingId)}`;
        });
    });

    // 새로 추가: Delete 버튼 이벤트 등록
    const deleteButtons = bookingListDiv.querySelectorAll('.deleteBtn');
    deleteButtons.forEach((btn) => {
        btn.addEventListener('click', (e) => {
            const bookingId = e.target.getAttribute('data-booking-id');
            deleteBooking(bookingId);
        });
    });
}



/***********************************************************
 * [3] Manage Booking 페이지 로직 (새로 추가)
 ***********************************************************/
/**
 * ManageBooking 페이지 초기화
 */

/***********************************************************
 * ManageBooking 페이지 초기화
 ***********************************************************/
function initManageBooking() {
    if (!isLoggedIn()) {
        window.location.href = "/diggydog/login.html";
        return;
    }

    const loggedInUserId = getLoggedInUserId();
    const userInfoDiv = document.getElementById('userInfo');
    if (userInfoDiv) {
        userInfoDiv.textContent = `Logged in as: ${loggedInUserId}`;
    }

    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', logout);
    }

    const updateBtn = document.getElementById('updateBtn');
    if (updateBtn) {
        updateBtn.addEventListener('click', updateBooking);
    }
    const deleteBtn = document.getElementById('deleteBtn');
    if (deleteBtn) {
        deleteBtn.addEventListener('click', deleteBooking);
    }

    // userId hidden
    document.getElementById('userId').value = loggedInUserId;

    // URL 파라미터
    const params = new URLSearchParams(window.location.search);
    const passedBookingId = params.get('bookingId');

    // 시/분 select 옵션 채우기
    populateHourMinuteSelects();

    // [추가] bookingId가 있으면 -> 서버로부터 상세정보 조회 후 populate
    if (passedBookingId) {
        document.getElementById('bookingId').value = passedBookingId;

        // 서버 단건조회 → 폼 세팅
        fetchBookingDetails(passedBookingId)
            .then((bookingData) => {
                populateFormFields(bookingData);
            })
            .catch((err) => {
                showMessage('#message', `Error loading booking: ${err.message}`, true);
            });
    }
}

/**
 * [추가 1] 단일 예약 정보 조회
 *  - 예) GET /api/booking/{bookingId}
 *  - 서버가 단건 조회를 지원한다고 가정
 */
async function fetchBookingDetails(bookingId) {
    try {
        const response = await fetch(`http://walab.handong.edu:8080/diggydog_3/api/booking/getBookingId?booking_id=${bookingId}`, {
            method: 'GET'
        });
        if (!response.ok) {
            throw new Error(`Failed to fetch booking. HTTP status: ${response.status}`);
        }
        // JSON 파싱
        const bookingData = await response.json();
        // bookingData 구조가 BookingResponseDTO라고 가정
        // 예: { bookingId: "...", userId: "...", date: "2025-01-25", bookingTime: "13:00", bookingInfo: "...", name: "...", noPeople: 4 }

        return bookingData;
    } catch (error) {
        console.error('Error fetching booking details:', error);
        throw error;
    }
}

/**
 * [추가 2] 폼 필드 세팅 함수
 *  - 서버로부터 받은 bookingData를 <span>, <select>, <input> 등에 넣어주는 로직
 */
function populateFormFields(bookingData) {
    // 1) hidden 필드
    document.getElementById('bookingId').value = bookingData.bookingId;
    document.getElementById('userId').value = bookingData.userId;

    // 2) date
    if (bookingData.date) {
        document.getElementById('date').value = bookingData.date; // ex) "2025-01-25"
    }

    // 3) bookingTime "HH:mm" -> 시/분 분리
    if (bookingData.bookingTime) {
        const [hh, mm] = bookingData.bookingTime.split(':');
        // 시/분 select 에 값 세팅
        document.getElementById('hourSelect').value = parseInt(hh, 10);
        document.getElementById('minuteSelect').value = parseInt(mm, 10);
    }

    // 4) bookingInfo
    if (bookingData.bookingInfo) {
        document.getElementById('bookingInfo').value = bookingData.bookingInfo;
    }

    // 5) name (span)
    if (bookingData.name) {
        document.getElementById('nameDisplay').textContent = bookingData.name;
    }

    // 6) noPeople
    if (typeof bookingData.noPeople === 'number') {
        document.getElementById('noPeople').value = bookingData.noPeople;
    }
}



/**
 * 시(hour) 0~23, 분(minute) 0~59 옵션 자동 생성
 */
function populateHourMinuteSelects() {
    const hourSelect = document.getElementById('hourSelect');
    const minuteSelect = document.getElementById('minuteSelect');
    if (!hourSelect || !minuteSelect) return;

    // hour: 0 ~ 23
    for (let h = 0; h < 24; h++) {
        const opt = document.createElement('option');
        opt.value = h;
        opt.textContent = h.toString().padStart(2, '0');
        hourSelect.appendChild(opt);
    }

    // minute: 0 ~ 59
    for (let m = 0; m < 60; m++) {
        const opt = document.createElement('option');
        opt.value = m;
        opt.textContent = m.toString().padStart(2, '0');
        minuteSelect.appendChild(opt);
    }
}

/***********************************************************
 * Update Booking (PATCH)
 ***********************************************************/
async function updateBooking() {
    const bookingId = document.getElementById('bookingId').value.trim();
    if (!bookingId) {
        showMessage('#message', 'No booking ID found. Cannot update.', true);
        return;
    }

    // 필드 값 가져오기
    const userId = document.getElementById('userId').value.trim();
    const dateValue = document.getElementById('date').value.trim();
    const hourValue = document.getElementById('hourSelect').value;
    const minuteValue = document.getElementById('minuteSelect').value;

    const bookingInfo = document.getElementById('bookingInfo').value.trim();
    // name은 <span>으로 표시 -> textContent
    const nameValue = document.getElementById('nameDisplay').textContent.trim();

    const noPeople = parseInt(document.getElementById('noPeople').value) || 0;

    // "HH:mm"
    const hh = hourValue.toString().padStart(2, '0');
    const mm = minuteValue.toString().padStart(2, '0');
    const bookingTime = `${hh}:${mm}`;

    // payload 구성
    const payload = {
        userId: userId,        // hidden
        date: dateValue,       // "YYYY-MM-DD"
        bookingTime: bookingTime, // "HH:mm"
        bookingInfo: bookingInfo,
        name: nameValue,       // span에서 가져옴
        noPeople: noPeople
    };

    try {
        const res = await fetch(`http://walab.handong.edu:8080/diggydog_3/api/booking/updateBooking?booking_id=${encodeURIComponent(bookingId)}`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const result = await res.json();
        if (res.ok) {
            showMessage('#message', 'Booking updated successfully!', false);
            console.log('Update result:', result);
        } else {
            showMessage('#message', result.message || 'Error updating booking', true);
        }
    } catch (error) {
        showMessage('#message', `Server error: ${error.message}`, true);
    }
}

/***********************************************************
 * Delete Booking (DELETE)
 ***********************************************************/
async function deleteBooking() {
    const bookingId = document.getElementById('bookingId').value.trim();
    if (!bookingId) {
        showMessage('#message', 'No booking ID found. Cannot delete.', true);
        return;
    }

    try {
        const res = await fetch(`http://walab.handong.edu:8080/diggydog_3/api/booking/deleteBooking?bookingId=${encodeURIComponent(bookingId)}`, {
            method: 'DELETE'
        });
        const result = await res.json();
        if (res.ok) {
            showMessage('#message', 'Booking deleted successfully!', false);
            console.log('Delete result:', result);
        } else {
            showMessage('#message', result.message || 'Error deleting booking', true);
        }
    } catch (error) {
        showMessage('#message', `Server error: ${error.message}`, true);
    }
}
