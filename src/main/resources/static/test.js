
// Spring Boot 방명록 API 주소
const API_URL = "/guestbook";


// ========================================
// 1. 방명록 목록 불러오기
// ========================================
async function loadGuestbooks() {
    try {
        const response = await fetch(`${API_URL}/get`);

        if (!response.ok) {
            throw new Error("방명록 조회 실패");
        }

        const guestbooks = await response.json();

        // 받아온 방명록을 화면에 표시
        displayGuestbooks(guestbooks);

    } catch (error) {
        console.error("조회 오류:", error);

        alert("방명록을 불러오지 못했습니다.");
    }
}


// ========================================
// 2. 방명록 화면에 표시하기
// ========================================
function displayGuestbooks(guestbooks) {
    const guestbookList = document.getElementById("guestbookList");

    // 기존 화면 내용 삭제
    guestbookList.innerHTML = "";

    // 방명록 하나씩 화면에 표시
    guestbooks.forEach(guestbook => {

        // ========================================
        // 작성일 형식 변경
        // ========================================
        // 예시:
        // 2026-09-14T15:32:45
        //
        // 화면:
        // 2026년09월14일15시32분
        // ========================================

        const date = new Date(guestbook.createdAt);

        const formattedDate =
            `${date.getFullYear()}년` +
            `${String(date.getMonth() + 1).padStart(2, "0")}월` +
            `${String(date.getDate()).padStart(2, "0")}일` +
            `${String(date.getHours()).padStart(2, "0")}시` +
            `${String(date.getMinutes()).padStart(2, "0")}분`;


        const div = document.createElement("div");


        div.innerHTML = `
            <hr>

            <h3>이름: ${guestbook.name}</h3>

            <p>평점: ${guestbook.rating}점</p>

            <p>내용: ${guestbook.content}</p>

            <p>작성일: ${formattedDate}</p>

            <!-- 수정 버튼 -->
            <button onclick="editGuestbook(${guestbook.id})">
                수정
            </button>

            <!-- 삭제 버튼 -->
            <button onclick="deleteGuestbook(${guestbook.id})">
                삭제
            </button>

            <!-- 수정 입력창이 표시될 공간 -->
            <div id="editForm-${guestbook.id}"></div>
        `;

        guestbookList.appendChild(div);
    });
}


// ========================================
// 3. 수정 버튼 클릭
// ========================================
function editGuestbook(id) {
    const editForm = document.getElementById(`editForm-${id}`);

    // 수정 입력창 표시
    editForm.innerHTML = `
        <br>

        <h3>방명록 수정</h3>

        <label>
            이름:
            <input
                type="text"
                id="editName-${id}"
                placeholder="수정할 이름"
            >
        </label>

        <br><br>

        <label>
            평점:
            <select id="editRating-${id}">
                <option value="1">1점</option>
                <option value="2">2점</option>
                <option value="3">3점</option>
                <option value="4">4점</option>
                <option value="5">5점</option>
            </select>
        </label>

        <br><br>

        <label>
            내용:
            <br>
            <textarea
                id="editContent-${id}"
                placeholder="수정할 내용을 입력하세요."
                rows="4"
                cols="30"
            ></textarea>
        </label>

        <br><br>

        <label>
            비밀번호:
            <input
                type="password"
                id="editPassword-${id}"
                maxlength="4"
                placeholder="기존 비밀번호"
            >
        </label>

        <br><br>

        <!-- 저장 버튼 -->
        <button onclick="updateGuestbook(${id})">
            저장
        </button>

        <!-- 취소 버튼 -->
        <button onclick="cancelEdit(${id})">
            취소
        </button>
    `;
}


// ========================================
// 4. 수정 취소
// ========================================
function cancelEdit(id) {
    const editForm = document.getElementById(`editForm-${id}`);

    // 수정 입력창 삭제
    editForm.innerHTML = "";
}


// ========================================
// 5. 수정 저장
// ========================================
async function updateGuestbook(id) {

    // 수정 입력값 가져오기
    const name = document.getElementById(`editName-${id}`).value;
    const rating = document.getElementById(`editRating-${id}`).value;
    const content = document.getElementById(`editContent-${id}`).value;
    const password = document.getElementById(`editPassword-${id}`).value;


    // 이름 확인
    if (name.trim() === "") {
        alert("이름을 입력해주세요.");
        return;
    }


    // 내용 확인
    if (content.trim() === "") {
        alert("내용을 입력해주세요.");
        return;
    }


    // 비밀번호 확인
    if (password.trim() === "") {
        alert("비밀번호를 입력해주세요.");
        return;
    }


    // Spring Boot로 보낼 데이터
    const guestbookData = {
        id: id,
        name: name,
        rating: Number(rating),
        content: content,
        password: password
    };


    try {
        const response = await fetch(`${API_URL}/put`, {
            method: "PUT",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(guestbookData)
        });


        const result = await response.text();


        if (response.ok) {

            alert("방명록이 수정되었습니다.");

            // 수정 후 목록 다시 불러오기
            loadGuestbooks();

        } else {

            alert(result || "수정에 실패했습니다.");

        }


    } catch (error) {

        console.error("수정 오류:", error);

        alert("서버와 연결할 수 없습니다.");

    }
}


// ========================================
// 6. 삭제
// ========================================
async function deleteGuestbook(id) {

    // 비밀번호 입력창
    const password = prompt("비밀번호를 입력해주세요.");


    // 취소를 누르면 종료
    if (password === null) {
        return;
    }


    // 빈 값 확인
    if (password.trim() === "") {
        alert("비밀번호를 입력해주세요.");
        return;
    }


    try {

        const response = await fetch(
            `${API_URL}/delete?id=${id}&password=${encodeURIComponent(password)}`,
            {
                method: "DELETE"
            }
        );


        const result = await response.text();


        if (response.ok) {

            alert(result);

            // 삭제 후 목록 다시 불러오기
            loadGuestbooks();

        } else {

            alert(result || "삭제에 실패했습니다.");

        }


    } catch (error) {

        console.error("삭제 오류:", error);

        alert("서버와 연결할 수 없습니다.");

    }
}


// ========================================
// 7. 페이지가 열리면 방명록 조회
// ========================================
loadGuestbooks();